package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.contract.ContractApiService;
import com.higgschain.trust.slave.api.enums.utxo.UTXOActionTypeEnum;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.datahandler.utxo.UTXOSnapshotHandler;
import com.higgschain.trust.slave.dao.mysql.account.CurrencyInfoDao;
import com.higgschain.trust.slave.dao.po.account.CurrencyInfoPO;
import com.higgschain.trust.slave.model.bo.action.UTXOAction;
import com.higgschain.trust.slave.model.bo.utxo.Sign;
import com.higgschain.trust.slave.model.bo.utxo.TxIn;
import com.higgschain.trust.slave.model.bo.utxo.UTXO;
import com.higgschain.trust.slave.model.convert.UTXOConvert;
import com.higgschain.trust.zkproof.EncryptAmount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Utxo context service.
 */
@Slf4j
@Service
public class UTXOContextService extends ContractApiService {

    @Autowired
    private UTXOSnapshotHandler utxoSnapshotHandler;

    @Autowired
    private CurrencyInfoDao currencyInfoDao;

    /**
     * Gets action.
     *
     * @return the action
     */
    public UTXOAction getAction() {
        return getContextData(UTXOExecuteContextData.class).getAction();
    }

    /**
     * get utxo action type
     *
     * @param name the name
     * @return utxo action type
     */
    public UTXOActionTypeEnum getUTXOActionType(String name) {
        return UTXOActionTypeEnum.getUTXOActionTypeEnumByName(name);
    }

    /**
     * query UTXO list
     *
     * @param inputList the input list
     * @return list
     */
    public List<UTXO> queryUTXOList(List<TxIn> inputList) {
        log.info("When process UTXO contract  querying queryTxOutList by inputList:{}", inputList);
        return utxoSnapshotHandler.queryUTXOList(inputList);
    }

    /**
     * 根据币种获取同态公钥
     *
     * @param currency the currency
     * @return currency homomorphic pk
     */
    public String getCurrencyHomomorphicPk(String currency) {
        log.info("get a homomorphic key when verify a crypto currency");
        CurrencyInfoPO currencyInfoPO = currencyInfoDao.queryByCurrency(currency);
        if (currencyInfoPO != null) {
            return currencyInfoPO.getHomomorphicPk();
        }
        return "";
    }

    /**
     * add two big decimal data
     * usually used by add  amount
     * return  augend + addend
     *
     * @param augend the augend
     * @param addend the addend
     * @return big decimal
     */
    public BigDecimal add(String augend, String addend) {
        BigDecimal a = null;
        BigDecimal b = null;
        try {
            a = new BigDecimal(augend);
            b = new BigDecimal(addend);
        } catch (Throwable e) {
            log.error("UTXO context NumberFormatException in for method add, when execute contract");
            throw new SlaveException(SlaveErrorEnum.SLAVE_UTXO_CONTRACT_PROCESS_FAIL_ERROR);
        }
        return a.add(b);
    }

    /**
     * subtract two data
     * usually used by subtract  amount
     * return  minuend -  reduction
     *
     * @param minuend   the minuend
     * @param reduction the reduction
     * @return big decimal
     */
    public BigDecimal subtract(String minuend, String reduction) {
        BigDecimal a = null;
        BigDecimal b = null;
        try {
            a = new BigDecimal(minuend);
            b = new BigDecimal(reduction);
        } catch (Throwable e) {
            log.error("UTXO context NumberFormatException in for method subtract, when execute contract");
            throw new SlaveException(SlaveErrorEnum.SLAVE_UTXO_CONTRACT_PROCESS_FAIL_ERROR);
        }
        return a.subtract(b);
    }

    /**
     * compare two big decimal
     * if a > b  then return 1
     * if a == b then return 0
     * if a < b then return -1
     *
     * @param a the a
     * @param b the b
     * @return int
     */
    public int compare(String a, String b) {
        BigDecimal a0 = null;
        BigDecimal b0 = null;
        try {
            a0 = new BigDecimal(a);
            b0 = new BigDecimal(b);
        } catch (Throwable e) {
            log.error("UTXO context NumberFormatException in for method compare, when execute contract");
            throw new SlaveException(SlaveErrorEnum.SLAVE_UTXO_CONTRACT_PROCESS_FAIL_ERROR);
        }
        return a0.compareTo(b0);
    }

    /**
     * verify UTXO Signature list
     * all the Signature is sign from the same message with different private key
     *
     * @param signList the sign list
     * @param message  the message
     * @return boolean
     */
    public boolean verifySignature(List<Sign> signList, String message) {
        if (CollectionUtils.isEmpty(signList) || null == message) {
            log.error("Verify UTXO Signature list for signList or message is null error!");
            return false;
        }
        for (Sign sign : signList) {
            if (StringUtils.isBlank(sign.getPubKey()) || StringUtils.isBlank(sign.getSignature())) {
                log.error("UTXO sign info :{} for PubKey or Signature is null error!", sign);
                return false;
            }
            if (!CryptoUtil.getBizCrypto(sign.getCryptoType()).verify(message, sign.getSignature(), sign.getPubKey())) {
                log.error("UTXO verify message :{} for Signature :{} with pubKey :{}  failed error!", message, sign.getSignature(), sign.getPubKey());
                return false;
            }
        }
        return true;
    }

    /**
     * Cipher compare int.
     *
     * @param a the a
     * @param b the b
     * @return the int
     */
    public int cipherCompare(String a, String b) {
        if (EncryptAmount.cipherCompare(a, b)) {
            return 0;
        }
        return 1;
    }

    /**
     * Init pub key.
     *
     * @param pubKey the pub key
     */
    public void initPubKey(String pubKey) {
        EncryptAmount.setHomomorphicEncryptionKey(pubKey);
    }

    /**
     * Cipher add string.
     *
     * @param em1 the em 1
     * @param em2 the em 2
     * @return the string
     */
    public String cipherAdd(String em1, String em2) {
        if (em1.equals("0")) {
            return em2;
        }
        return EncryptAmount.cipherAdd(em1, em2);
    }

    /**
     * 初始发币加密
     *
     * @param amount the amount
     * @return string
     */
    public String issueEnrypt(String amount) {
        EncryptAmount encryptAmount = new EncryptAmount(new BigDecimal(amount), EncryptAmount.FULL_RANDOM);
        return encryptAmount.toString();
    }

    /**
     * Verify tx in signature boolean.
     *
     * @param signList the sign list
     * @param txInList the tx in list
     * @return the boolean
     */
    public boolean verifyTxInSignature(List<Sign> signList, List<TxIn> txInList) {
        String message = UTXOConvert.toTxInString(txInList);
        return verifySignature(signList, message);
    }


}

