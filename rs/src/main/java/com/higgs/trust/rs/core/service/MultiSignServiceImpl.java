package com.higgs.trust.rs.core.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.higgs.trust.evmcontract.crypto.ECKey;
import com.higgs.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgs.trust.rs.common.exception.RsCoreException;
import com.higgs.trust.rs.common.utils.CoreTransactionConvertor;
import com.higgs.trust.rs.core.api.ContractV2QueryService;
import com.higgs.trust.rs.core.api.CoreTransactionService;
import com.higgs.trust.rs.core.api.MultiSignService;
import com.higgs.trust.rs.core.api.RsBlockChainService;
import com.higgs.trust.rs.core.vo.MultiSignHashVO;
import com.higgs.trust.rs.core.vo.MultiSignRuleVO;
import com.higgs.trust.rs.core.vo.MultiSignTxVO;
import com.higgs.trust.slave.api.enums.ActionTypeEnum;
import com.higgs.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgs.trust.slave.api.vo.RespData;
import com.higgs.trust.slave.model.bo.CoreTransaction;
import com.higgs.trust.slave.model.bo.contract.ContractCreationV2Action;
import com.higgs.trust.slave.model.bo.contract.ContractInvokeV2Action;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.collections.Lists;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import static com.higgs.trust.rs.common.enums.RsCoreErrorEnum.RS_CORE_CONTRACT_READ_ERROR;

/**
 * @author liuyu
 * @description
 * @date 2019-03-20
 */
@Service @Slf4j public class MultiSignServiceImpl implements MultiSignService {
    private final static int SCALE_NUMBER = 8;
    private final static String CONTRACT_CONSTRUCTOR_NAME = "MultiSign";
    private final static String METHOD_GET_SIGN_HASH = "(bytes32) getSourceHash(address,address,uint)";
    private final static String METHOD_TRANSFER = "(bool) transfer(address,uint256,bool,bytes)";
    /**
     * config path
     */
    @Value("$rs.contract.multi-sign.path") String contractCodePath;

    @Autowired CoreTransactionConvertor coreTransactionConvertor;
    @Autowired CoreTransactionService coreTransactionService;
    @Autowired ContractV2QueryService contractV2QueryService;
    @Autowired RsBlockChainService rsBlockChainService;

    @Override public RespData<String> createAddress(MultiSignRuleVO rule) throws RsCoreException {
        log.info("createAddress rule:{}", rule);
        String contractHexCode = null;
        try {
            log.info("createAddress contractCodePath:{}", contractCodePath);
            //get contract code from file path
            contractHexCode = FileUtils.readFileToString(new File(contractCodePath), Charsets.UTF_8);
        } catch (Exception e) {
            log.error("createAddress has error,read contract code is error", e);
            throw new RsCoreException(RS_CORE_CONTRACT_READ_ERROR);
        }
        //build contract code
        contractHexCode = coreTransactionConvertor
            .buildContractCode(contractHexCode, CONTRACT_CONSTRUCTOR_NAME, rule.getAddrs(), rule.getVerifyNum(),
                rule.getMustAddrs());
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

    @Override public RespData<String> getSignHashValue(MultiSignHashVO vo) throws RsCoreException {
        BigInteger amount = vo.getAmount().scaleByPowerOfTen(SCALE_NUMBER).toBigInteger();
        log.info("getSignHashValue amount:{}", amount);
        List<?> result = contractV2QueryService
            .query(null, vo.getFromAddr(), METHOD_GET_SIGN_HASH, vo.getFromAddr(), vo.getToAddr(), amount);
        if (CollectionUtils.isEmpty(result) || result.get(0) == null) {
            log.info("getSignHashValue result is empty");
            return RespData.error(RsCoreErrorEnum.RS_CORE_CONTRACT_EXECUTE_ERROR.getCode(),
                RsCoreErrorEnum.RS_CORE_CONTRACT_EXECUTE_ERROR.getDescription(), null);
        }
        log.info("getSignHashValue vo:{},result:{}", vo, result);
        return new RespData<>(Hex.toHexString((byte[])result.get(0)));
    }

    @Override public RespData<Boolean> transfer(MultiSignTxVO vo) throws RsCoreException {
        log.info("transfer vo:{}", vo);
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
        List<String> signs = vo.getSigns();
        signs.forEach(v -> {
            sb.append(v);
        });
        action.setArgs(new Object[] {vo.getToAddr(), amount, true, Hex.decode(sb.toString())});
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
