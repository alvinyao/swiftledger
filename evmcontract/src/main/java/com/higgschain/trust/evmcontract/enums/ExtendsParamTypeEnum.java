package com.higgschain.trust.evmcontract.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * extends param type
 * @author tangkun
 * @date 2019-04-17
 */
public enum ExtendsParamTypeEnum {

    //hex str of 'policy_id'
    POLICY_ID("0000000000000000000000000000000000000000000000706f6c6963795f6964","policy_id"),

    //hex str of 'tx_id'
    TX_ID("00000000000000000000000000000000000000000000000000000074785f6964","tx_id"),
    ;

    /**
     * type code
     * type detail desc
     */
    @Getter
    private String code;

    @Setter
    private String desc;

    ExtendsParamTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
