package com.higgschain.trust.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * crypto enum
 *
 * @author lingchao
 * @create 2018年09月14日14 :29
 */
@Getter
public enum CryptoTypeEnum {/**
 * The Rsa.
 */
RSA("RSA", "rsa type"),
    /**
     * The Sm.
     */
    SM("SM", "sm type"),
    /**
     * The Ecc.
     */
    ECC("ECC", "ecc type");

    CryptoTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    /**
     * get CryptoTypeEnum by code
     *
     * @param code the code
     * @return by code
     */
    public static CryptoTypeEnum getByCode(String code) {
        for (CryptoTypeEnum cryptoTypeEnum : CryptoTypeEnum.values()) {
            if (StringUtils.equals(cryptoTypeEnum.getCode(), code)) {
                return cryptoTypeEnum;
            }
        }
        return null;
    }

}
