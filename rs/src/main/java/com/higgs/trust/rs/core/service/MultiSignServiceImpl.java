package com.higgs.trust.rs.core.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.higgs.trust.evmcontract.crypto.ECKey;
import com.higgs.trust.rs.common.exception.RsCoreException;
import com.higgs.trust.rs.common.utils.CoreTransactionConvertor;
import com.higgs.trust.rs.core.api.CoreTransactionService;
import com.higgs.trust.rs.core.api.MultiSignService;
import com.higgs.trust.rs.core.vo.MultiSignRuleVO;
import com.higgs.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgs.trust.slave.api.vo.RespData;
import com.higgs.trust.slave.model.bo.CoreTransaction;
import com.higgs.trust.slave.model.bo.contract.ContractCreationV2Action;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import java.io.File;

import static com.higgs.trust.rs.common.enums.RsCoreErrorEnum.RS_CORE_CONTRACT_READ_ERROR;

/**
 * @author liuyu
 * @description
 * @date 2019-03-20
 */
@Service @Slf4j public class MultiSignServiceImpl implements MultiSignService {
    private final static String CONTRACT_CONSTRUCTOR_NAME = "MultiSign";
    /**
     * config path
     */
    @Value("$rs.contract.multi-sign.path") String contractCodePath;

    @Autowired CoreTransactionConvertor coreTransactionConvertor;
    @Autowired CoreTransactionService coreTransactionService;

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
}
