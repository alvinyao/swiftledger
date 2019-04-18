package com.higgschain.trust.evmcontract.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * extends param type
 * @author tangkun
 * @date 2019-04-17
 */
public enum ExtendsParamTypeEnum {

    POLICY_ID("policy_id","tx policy id");

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
