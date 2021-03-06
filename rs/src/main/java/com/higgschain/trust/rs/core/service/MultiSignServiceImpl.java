package com.higgschain.trust.rs.core.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.evmcontract.crypto.ECKey;
import com.higgschain.trust.evmcontract.facade.exception.ContractExecutionException;
import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.common.utils.CoreTransactionConvertor;
import com.higgschain.trust.rs.core.api.ContractV2QueryService;
import com.higgschain.trust.rs.core.api.CoreTransactionService;
import com.higgschain.trust.rs.core.api.MultiSignService;
import com.higgschain.trust.rs.core.api.RsBlockChainService;
import com.higgschain.trust.rs.core.vo.CreateCurrencyVO;
import com.higgschain.trust.rs.core.vo.MultiSignHashVO;
import com.higgschain.trust.rs.core.vo.MultiSignRuleVO;
import com.higgschain.trust.rs.core.vo.MultiSignTxVO;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.account.IssueCurrency;
import com.higgschain.trust.slave.model.bo.contract.ContractCreationV2Action;
import com.higgschain.trust.slave.model.bo.contract.ContractInvokeV2Action;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.testng.collections.Lists;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Multi sign service.
 *
 * @author liuyu
 * @description
 * @date 2019 -03-20
 */
@Service @Slf4j public class MultiSignServiceImpl implements MultiSignService {
    private final static int SCALE_NUMBER = 8;
    private final static String MULTI_SIGN_CONTRACT_CONSTRUCTOR_NAME = "MultiSign(address[],uint,address[])";
    private final static String CURRENCY_CONTRACT_CONSTRUCTOR_NAME = "Token(address,string,uint)";
    private final static String METHOD_GET_SIGN_HASH = "(bytes32) getSourceHash(address,address,uint)";
    private final static String METHOD_TRANSFER = "(bool) transfer(address,uint256,bool,string)";
    /**
     * config path
     */
    @Value("${rs.contract.multi-sign.path:/data/home/admin/trust/multi-sign-temp.sol}") String multiContractCodePath;
    /**
     * The Currency contract code path.
     */
    @Value("${rs.contract.currency.path:/data/home/admin/trust/currency-temp.sol}") String currencyContractCodePath;

    /**
     * The Core transaction convertor.
     */
    @Autowired CoreTransactionConvertor coreTransactionConvertor;
    /**
     * The Core transaction service.
     */
    @Autowired CoreTransactionService coreTransactionService;
    /**
     * The Contract v 2 query service.
     */
    @Autowired ContractV2QueryService contractV2QueryService;
    /**
     * The Rs block chain service.
     */
    @Autowired RsBlockChainService rsBlockChainService;

    @Override public RespData<String> createAddress(MultiSignRuleVO rule) throws RsCoreException {
        log.info("createAddress rule:{}", rule);
        List<String> addrs = rule.getAddrs();
        List<String> mustAddrs = rule.getMustAddrs();
        if (!CollectionUtils.isEmpty(mustAddrs)) {
            //check size
            if (mustAddrs.size() > addrs.size()) {
                log.info("createAddress mustAddrs.size can`t greater than addrs.size");
                throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_BUILD_ERROR);
            }
            //check exist
            List<String> collect = mustAddrs.stream().filter(a -> addrs.contains(a)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)) {
                log.info("createAddress mustAddrs.item can`t found in addrs");
                throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_BUILD_ERROR);
            }
        }
        String contractHexCode;
        try {
            log.info("createAddress contractCodePath:{}", multiContractCodePath);
            //get contract code from file path
            contractHexCode = FileUtils.readFileToString(new File(multiContractCodePath), Charsets.UTF_8);
        } catch (Exception e) {
            log.error("createAddress has error,read contract code is error", e);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_READ_ERROR);
        }
        if (StringUtils.isEmpty(contractHexCode)) {
            log.error("createAddress has error,contract code is empty");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_READ_ERROR);
        }
        log.info("createAddress contract code is:{}", contractHexCode);
        //build contract code
        contractHexCode = coreTransactionConvertor
            .buildContractCode(contractHexCode, MULTI_SIGN_CONTRACT_CONSTRUCTOR_NAME, rule.getAddrs(),
                rule.getVerifyNum(), rule.getMustAddrs());
        //create contract address
        String contractAddress = Hex.toHexString(new ECKey().getAddress());
        log.info("createAddress contractAddress:{}", contractAddress);
        //make action
        ContractCreationV2Action contractCreationV2Action = coreTransactionConvertor
            .buildContractCreationV2Action(contractAddress, contractAddress, contractHexCode, 0);
        //make core-transaction
        CoreTransaction coreTransaction = coreTransactionConvertor
            .buildCoreTransaction(rule.getRequestId(), new JSONObject(), Lists.newArrayList(contractCreationV2Action),
                InitPolicyEnum.CONTRACT_ISSUE.getPolicyId());
        //submit tx
        coreTransactionService.submitTx(coreTransaction);
        //wait for the results of the cluster
        RespData respData = coreTransactionService.syncWait(coreTransaction.getTxId(), true);
        log.info("createAddress result:{}", respData);
        //check result
        if (!respData.isSuccess()) {
            return new RespData<>(respData.getRespCode(), respData.getMsg());
        }
        //return address
        return RespData.success(contractAddress);
    }

    @Override public RespData<Boolean> createCurrencyContract(CreateCurrencyVO vo) throws RsCoreException {
        log.info("createCurrencyContract vo:{}", vo);
        String contractAddress = Hex.toHexString(new ECKey().getAddress());
        log.info("createCurrencyContract contractAddress:{}", contractAddress);
        String contractHexCode;
        try {
            log.info("createCurrencyContract currencyContractCodePath:{}", currencyContractCodePath);
            //get contract code from file path
            contractHexCode = FileUtils.readFileToString(new File(currencyContractCodePath), Charsets.UTF_8);
        } catch (Exception e) {
            log.error("createCurrencyContract has error,read contract code is error", e);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_READ_ERROR);
        }
        if (StringUtils.isEmpty(contractHexCode)) {
            log.error("createCurrencyContract has error,contract code is empty");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_READ_ERROR);
        }
        log.debug("createCurrencyContract contract code is:{}", contractHexCode);
        BigInteger amount = vo.getAmount().scaleByPowerOfTen(SCALE_NUMBER).toBigInteger();
        //build contract code
        contractHexCode = coreTransactionConvertor
            .buildContractCode(contractHexCode, CURRENCY_CONTRACT_CONSTRUCTOR_NAME, vo.getAddress(), vo.getCurrency(),
                amount);

        //make actions
        IssueCurrency issueCurrencyAction =
            coreTransactionConvertor.buildIssueCurrencyAction(vo.getCurrency(), 0, contractAddress, null, null);
        ContractCreationV2Action contractCreationV2Action = coreTransactionConvertor
            .buildContractCreationV2Action(vo.getAddress(), contractAddress, contractHexCode, 1);

        //make core-transaction
        CoreTransaction coreTransaction = coreTransactionConvertor
            .buildCoreTransaction(vo.getRequestId(), new JSONObject(),
                Lists.newArrayList(issueCurrencyAction, contractCreationV2Action),
                InitPolicyEnum.CONTRACT_INVOKE.getPolicyId());
        //submit tx
        coreTransactionService.submitTx(coreTransaction);
        //wait for the results of the cluster
        RespData respData = coreTransactionService.syncWait(coreTransaction.getTxId(), true);
        log.info("createCurrencyContract result:{}", respData);
        //check result
        if (!respData.isSuccess()) {
            return new RespData<>(respData.getRespCode(), respData.getMsg());
        }
        return RespData.success(true);
    }

    @Override public RespData<String> getSignHashValue(MultiSignHashVO vo) throws RsCoreException {
        log.info("getSignHashValue vo:{}", vo);
        BigInteger amount = vo.getAmount().scaleByPowerOfTen(SCALE_NUMBER).toBigInteger();
        log.info("getSignHashValue amount:{}", amount);
        String contractAddress;
        if (vo.isMultiSign()) {
            contractAddress = vo.getFromAddr();
        } else {
            if (StringUtils.isEmpty(vo.getCurrency())) {
                log.info("transfer get trade contract address is fail,currency is empty");
                return RespData.error(RsCoreErrorEnum.RS_CORE_GET_CONTRACT_ADDR_BY_CURRENCY_ERROR.getCode(),
                    RsCoreErrorEnum.RS_CORE_GET_CONTRACT_ADDR_BY_CURRENCY_ERROR.getDescription(), null);
            }
            //get contract address for trade
            contractAddress = rsBlockChainService.queryContractAddressByCurrency(vo.getCurrency());
            if (StringUtils.isEmpty(contractAddress)) {
                log.info("transfer get trade contract address is fail,currency:{}", vo.getCurrency());
                return RespData.error(RsCoreErrorEnum.RS_CORE_GET_CONTRACT_ADDR_BY_CURRENCY_ERROR.getCode(),
                    RsCoreErrorEnum.RS_CORE_GET_CONTRACT_ADDR_BY_CURRENCY_ERROR.getDescription(), null);
            }
        }
        List<?> result;
        try {
            result =  contractV2QueryService
                .query(null, contractAddress, METHOD_GET_SIGN_HASH, vo.getFromAddr(), vo.getToAddr(), amount);
        }catch(ContractExecutionException e){
            log.error("getSignHashValue has error",e);
            return RespData.error(RsCoreErrorEnum.RS_CORE_CONTRACT_EXECUTE_ERROR.getCode(),
                e.getMessage(), null);
        }
        if (CollectionUtils.isEmpty(result) || result.get(0) == null) {
            log.info("getSignHashValue result is empty");
            return RespData.error(RsCoreErrorEnum.RS_CORE_CONTRACT_EXECUTE_ERROR.getCode(),
                RsCoreErrorEnum.RS_CORE_CONTRACT_EXECUTE_ERROR.getDescription(), null);
        }
        log.info("getSignHashValue result:{}", result);
        return RespData.success((String)result.get(0));
    }

    @Override public RespData<Boolean> transfer(MultiSignTxVO vo) throws RsCoreException {
        log.info("transfer vo:{}", vo);
        if (vo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("transfer amount is illegal:{}", vo.getAmount());
            return RespData.error(RsCoreErrorEnum.RS_CORE_CONTRACT_AMOUNT_IS_ILLEGAL.getCode(),
                RsCoreErrorEnum.RS_CORE_CONTRACT_AMOUNT_IS_ILLEGAL.getDescription(), null);
        }
        if (CollectionUtils.isEmpty(vo.getSigns())) {
            log.info("transfer signs is empty:{}", vo.getSigns());
            return RespData.error(RsCoreErrorEnum.RS_CORE_CONTRACT_SIGNS_IS_NOT_EMPTY.getCode(),
                RsCoreErrorEnum.RS_CORE_CONTRACT_SIGNS_IS_NOT_EMPTY.getDescription(), null);
        }
        //make action
        ContractInvokeV2Action action = new ContractInvokeV2Action();
        action.setIndex(0);
        action.setType(ActionTypeEnum.CONTRACT_INVOKED);
        //the multi-sign contract address
        action.setFrom(vo.getFromAddr());
        //get contract address for trade
        String tradeContractAddress = rsBlockChainService.queryContractAddressByCurrency(vo.getCurrency());
        if (StringUtils.isEmpty(tradeContractAddress)) {
            log.info("transfer get trade contract address is fail,currency:{}", vo.getCurrency());
            return RespData.error(RsCoreErrorEnum.RS_CORE_GET_CONTRACT_ADDR_BY_CURRENCY_ERROR.getCode(),
                RsCoreErrorEnum.RS_CORE_GET_CONTRACT_ADDR_BY_CURRENCY_ERROR.getDescription(), null);
        }
        //trade contract address
        action.setTo(tradeContractAddress);
        action.setMethodSignature(METHOD_TRANSFER);
        BigInteger amount = vo.getAmount().scaleByPowerOfTen(SCALE_NUMBER).toBigInteger();
        StringBuilder sb = new StringBuilder();
        vo.getSigns().forEach(v -> {
            sb.append(v);
        });
        action.setArgs(new Object[] {vo.getToAddr(), amount, vo.isMultiSign(), sb.toString()});
        log.info("transfer action:{}", action);
        //make core-transaction
        CoreTransaction coreTransaction = coreTransactionConvertor
            .buildCoreTransaction(vo.getRequestId(), new JSONObject(), Lists.newArrayList(action),
                InitPolicyEnum.CONTRACT_INVOKE.getPolicyId());
        //submit tx
        coreTransactionService.submitTx(coreTransaction);
        //wait for the results of the cluster
        RespData respData = coreTransactionService.syncWait(coreTransaction.getTxId(), true);
        log.info("transfer result:{}", respData);
        //check result
        if (!respData.isSuccess()) {
            return new RespData<>(respData.getRespCode(), respData.getMsg());
        }
        return RespData.success(true);
    }
}
