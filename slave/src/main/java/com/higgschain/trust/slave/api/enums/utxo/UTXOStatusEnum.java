package com.higgschain.trust.slave.api.enums.utxo;

import lombok.Getter;

/**
 * utxo status enum
 *
 * @author lingchao
 * @create 2018年03月27日16 :53
 */
@Getter public enum UTXOStatusEnum {/**
 * The Unspent.
 */
UNSPENT("UNSPENT", "it is utxo"),
    /**
     * The Spent.
     */
    SPENT("SPENT", "is is STXO"),;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    UTXOStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
