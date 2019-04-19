package com.higgschain.trust.evmcontract.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * extends param type
 * @author tangkun
 * @date 2019-04-17
 */
public enum ExtendsParamTypeEnum {

    //'policy_id' hex str
    POLICY_ID("0000000000000000000000000000000000000000000000706f6c6963795f6964","tx policy id");

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
