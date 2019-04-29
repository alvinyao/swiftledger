package com.higgschain.trust.consensus.util;

import com.higgschain.trust.common.crypto.Crypto;
import com.higgschain.trust.common.crypto.ecc.EccCrypto;
import com.higgschain.trust.common.crypto.gm.GmCrypto;
import com.higgschain.trust.common.crypto.rsa.RsaCrypto;
import com.higgschain.trust.common.enums.CryptoTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * The type Crypto util.
 *
 * @author WangQuanzhou
 * @desc crypto selector class
 * @date 2018 /8/15 15:25
 */
@Order(1) @Component @Slf4j public class CryptoUtil {

    /**
     * The constant bizCryptoType.
     */
    public static String bizCryptoType;
    /**
     * The constant consensusCryptoType.
     */
    public static String consensusCryptoType;

    /**
     * Gets biz crypto.
     *
     * @param cryptoType the crypto type
     * @return the biz crypto
     */
    public static Crypto getBizCrypto(String cryptoType) {
        if (log.isDebugEnabled()) {
            log.trace("crypto type for biz layer is {}", bizCryptoType);
        }
        Crypto crypto = StringUtils.isBlank(cryptoType) ? selector(bizCryptoType) : selector(cryptoType);
        return crypto;
    }

    /**
     * Gets protocol crypto.
     *
     * @return the protocol crypto
     */
    public static Crypto getProtocolCrypto() {
        if (log.isDebugEnabled()) {
            log.trace("crypto type for consensus layer is {}", consensusCryptoType);
        }
        return selector(consensusCryptoType);
    }

    private static Crypto selector(String usage) {
       CryptoTypeEnum cryptoTypeEnum = CryptoTypeEnum.getByCode(usage);
        switch (cryptoTypeEnum) {
            case RSA:
                return RsaCrypto.getSingletonInstance();
            case SM:
                return GmCrypto.getSingletonInstance();
            case ECC:
                return EccCrypto.getSingletonInstance();
            default:
        }
        return null;
    }

    /**
     * Sets biz.
     *
     * @param newBiz the new biz
     */
    @NotNull @Value("${higgs.trust.crypto.biz:RSA}") public void setBiz(String newBiz) {
        log.info("set biz,newBiz={}", newBiz);
        bizCryptoType = newBiz;
    }

    /**
     * Sets consensus.
     *
     * @param newConsensus the new consensus
     */
    @NotNull @Value("${higgs.trust.crypto.consensus:RSA}") public void setConsensus(String newConsensus) {
        log.info("set biz,newConsensus={}", newConsensus);
        consensusCryptoType = newConsensus;
    }

}
