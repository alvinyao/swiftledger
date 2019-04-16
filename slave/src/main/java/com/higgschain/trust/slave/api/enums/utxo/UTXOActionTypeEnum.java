package com.higgschain.trust.slave.api.enums.utxo;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * UTXO ActionPO Type enum
 *
 * @author lingchao
 * @create 2018年03月27日16 :53
 */
@Getter public enum UTXOActionTypeEnum {/**
 * The Issue.
 */
ISSUE("ISSUE", "issue the state UTXO action, need all partners to sign the transaction"),
    /**
     * The Normal.
     */
    NORMAL("NORMAL", "normal UTXO action , need  partners to sign the transaction"),
    /**
     * The Destruction.
     */
    DESTRUCTION("DESTRUCTION",
        "destruction UTXO action, need the owner to sign the transaction"),
    /**
     * The Crypto issue.
     */
    CRYPTO_ISSUE("CRYPTO_ISSUE","the same as issue"),
    /**
     * The Crypto normal.
     */
    CRYPTO_NORMAL("CRYPTO_NORMAL","the same as normal");

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    UTXOActionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets utxo action type enum by name.
     *
     * @param name the name
     * @return the utxo action type enum by name
     */
    public static UTXOActionTypeEnum getUTXOActionTypeEnumByName(String name) {
        for (UTXOActionTypeEnum utxoActionTypeEnum : UTXOActionTypeEnum.values()) {
            if (StringUtils.equals(utxoActionTypeEnum.name(), name)) {
                return utxoActionTypeEnum;
            }
        }
        return null;
    }
}
