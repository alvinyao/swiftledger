package com.higgschain.trust.rs.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.evmcontract.solidity.Abi;
import com.higgschain.trust.evmcontract.solidity.compiler.CompilationResult;
import com.higgschain.trust.evmcontract.solidity.compiler.SolidityCompiler;
import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.api.RsBlockChainService;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.api.enums.TxTypeEnum;
import com.higgschain.trust.slave.api.enums.VersionEnum;
import com.higgschain.trust.slave.api.enums.utxo.UTXOActionTypeEnum;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.account.IssueCurrency;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.action.DataIdentityAction;
import com.higgschain.trust.slave.model.bo.action.UTXOAction;
import com.higgschain.trust.slave.model.bo.contract.ContractCreationV2Action;
import com.higgschain.trust.slave.model.bo.utxo.TxIn;
import com.higgschain.trust.slave.model.bo.utxo.TxOut;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import static com.higgschain.trust.evmcontract.solidity.compiler.SolidityCompiler.Options.*;

/**
 * CoreTransaction Convertor
 *
 * @author lingchao
 * @create 2018年06月27日22 :58
 */
@Slf4j @Service public class CoreTransactionConvertor {
    @Autowired private NodeState nodeState;
    @Autowired private RsBlockChainService rsBlockChainService;

    /**
     * build core transaction
     *
     * @param txId       the tx id
     * @param bizModel   the biz model
     * @param actionList the action list
     * @param policyId   the policy id
     * @return core transaction
     */
    public CoreTransaction buildCoreTransaction(String txId, JSONObject bizModel, List<Action> actionList,
        String policyId) {
        return buildCoreTransaction(txId, bizModel, actionList, policyId, TxTypeEnum.DEFAULT.getCode());
    }

    /**
     * build core transaction
     *
     * @param txId       the tx id
     * @param bizModel   the biz model
     * @param actionList the action list
     * @param policyId   the policy id
     * @param txType     the tx type
     * @return core transaction
     */
    public CoreTransaction buildCoreTransaction(String txId, JSONObject bizModel, List<Action> actionList,
        String policyId, String txType) {
        CoreTransaction coreTransaction = new CoreTransaction();
        coreTransaction.setTxId(txId);
        coreTransaction.setBizModel(bizModel);
        coreTransaction.setActionList(actionList);
        coreTransaction.setVersion(VersionEnum.V1.getCode());
        coreTransaction.setSender(nodeState.getNodeName());
        coreTransaction.setSendTime(new Date());
        coreTransaction.setPolicyId(policyId);
        coreTransaction.setTxType(txType);
        return coreTransaction;
    }

    /**
     * build txIn
     *
     * @param txId        the tx id
     * @param actionIndex the action index
     * @param index       the index
     * @return tx in
     */
    public TxIn buildTxIn(String txId, Integer actionIndex, Integer index) {
        TxIn txIn = new TxIn();
        txIn.setTxId(txId);
        txIn.setActionIndex(actionIndex);
        txIn.setIndex(index);
        return txIn;
    }

    /**
     * build txOut
     *
     * @param identity    the identity
     * @param actionIndex the action index
     * @param index       the index
     * @param state       the state
     * @return tx out
     */
    public TxOut buildTxOut(String identity, Integer actionIndex, Integer index, JSONObject state) {
        TxOut txOut = new TxOut();
        txOut.setIdentity(identity);
        txOut.setActionIndex(actionIndex);
        txOut.setIndex(index);
        txOut.setState(state);
        return txOut;
    }

    /**
     * build dataIdentityAction
     *
     * @param identity the identity
     * @param index    the index
     * @return data identity action
     */
    public DataIdentityAction buildDataIdentityAction(String identity, int index) {
        DataIdentityAction dataIdentityAction = new DataIdentityAction();
        dataIdentityAction.setDataOwner(nodeState.getNodeName());
        dataIdentityAction.setChainOwner(rsBlockChainService.queryChainOwner());
        dataIdentityAction.setIdentity(identity);
        dataIdentityAction.setIndex(index);
        dataIdentityAction.setType(ActionTypeEnum.CREATE_DATA_IDENTITY);
        return dataIdentityAction;
    }

    /**
     * build UTXOAction
     *
     * @param utxoActionTypeEnum the utxo action type enum
     * @param contractAddress    the contract address
     * @param stateClass         the state class
     * @param index              the index
     * @param inputList          the input list
     * @param txOutList          the tx out list
     * @return utxo action
     */
    public UTXOAction buildUTXOAction(UTXOActionTypeEnum utxoActionTypeEnum, String contractAddress, String stateClass,
        int index, List<TxIn> inputList, List<TxOut> txOutList) {
        UTXOAction utxoAction = new UTXOAction();
        utxoAction.setInputList(inputList);
        utxoAction.setOutputList(txOutList);
        utxoAction.setContractAddress(contractAddress);
        utxoAction.setType(ActionTypeEnum.UTXO);
        utxoAction.setStateClass(stateClass);
        utxoAction.setUtxoActionType(utxoActionTypeEnum);
        utxoAction.setIndex(index);
        return utxoAction;
    }

    /**
     * build currency action
     *
     * @param currency        the currency
     * @param index           the index
     * @param contractAddress the contract address
     * @param homomorphicPk   the homomorphic pk
     * @param remark          the remark
     * @return issue currency
     */
    public IssueCurrency buildIssueCurrencyAction(String currency, int index, String contractAddress,
        String homomorphicPk, String remark) {
        IssueCurrency currencyAction = new IssueCurrency();
        currencyAction.setCurrencyName(currency);
        currencyAction.setIndex(index);
        currencyAction.setContractAddress(contractAddress);
        currencyAction.setHomomorphicPk(homomorphicPk);
        currencyAction.setRemark(remark);
        currencyAction.setType(ActionTypeEnum.ISSUE_CURRENCY);
        return currencyAction;
    }

    /**
     * build currency action
     *
     * @param currency the currency
     * @param index    the index
     * @param remark   the remark
     * @return issue currency
     */
    public IssueCurrency buildIssueCurrencyAction(String currency, int index, String remark) {
        IssueCurrency currencyAction = new IssueCurrency();
        currencyAction.setCurrencyName(currency);
        currencyAction.setRemark(remark);
        currencyAction.setType(ActionTypeEnum.ISSUE_CURRENCY);
        currencyAction.setIndex(index);
        return currencyAction;
    }

    /**
     * build contract create v2 action
     *
     * @param fromAddress     the from address
     * @param contractAddress the contract address
     * @param contractHexCode the contract hex code
     * @param actionIndex     the action index
     * @return contract creation v 2 action
     */
    public ContractCreationV2Action buildContractCreationV2Action(String fromAddress, String contractAddress,
        String contractHexCode, int actionIndex) {
        ContractCreationV2Action contractCreationV2Action = new ContractCreationV2Action();
        contractCreationV2Action.setFrom(fromAddress);
        contractCreationV2Action.setTo(contractAddress);
        contractCreationV2Action.setCode(contractHexCode);
        contractCreationV2Action.setVersion(VersionEnum.V1.getCode());
        contractCreationV2Action.setIndex(actionIndex);
        contractCreationV2Action.setType(ActionTypeEnum.CONTRACT_CREATION);
        return contractCreationV2Action;
    }

    /**
     * Build contract code string.
     *
     * @param in               the in
     * @param contractor       the contractor
     * @param policyId       the policyId
     * @param contractInitArgs the contract init args
     * @return the string
     */
    public String buildContractCode(InputStream in, String contractor,String policyId, Object... contractInitArgs) {
        try {
            String sourceCode = IOUtils.toString(in, Charsets.UTF_8);
            return buildContractCode(sourceCode, contractor,policyId, contractInitArgs);
        } catch (IOException e) {
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_READ_ERROR, e);
        }
    }

    /**
     * Build contract code string.
     *
     * @param sourceCode       the source code
     * @param contractor       the contractor
     * @param policyId         the policyId
     * @param contractInitArgs the contract init args
     * @return the string
     */
    public String buildContractCode(String sourceCode, String contractor,String policyId, Object... contractInitArgs) {
        try {
            if(!StringUtils.isEmpty(policyId) && contractInitArgs != null){
                byte[] bytes= Hex.encode(policyId.getBytes(StandardCharsets.UTF_8));
                byte[] policyIdBytes32 = null;
                int len = 32 - bytes.length;
                if (len>0){
                    policyIdBytes32 = new byte[32];
                    System.arraycopy(bytes,0,policyIdBytes32,len,bytes.length);
                }
//                Hex.toHexString(policyIdBytes32);
                contractInitArgs[contractInitArgs.length] = policyIdBytes32;
            }
            SolidityCompiler.Result res =
                SolidityCompiler.compile(sourceCode.getBytes(), true, ABI, BIN, INTERFACE, METADATA);
            CompilationResult result = CompilationResult.parse(res.output);
            CompilationResult.ContractMetadata metadata = result.getContract(Abi.Function.of(contractor).name);
            byte[] codeBytes = Abi.Constructor.of(contractor, Hex.decode(metadata.bin), contractInitArgs);
            return Hex.toHexString(codeBytes);
        } catch (Throwable e) {
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_CONTRACT_BUILD_ERROR, e);
        }
    }

}
