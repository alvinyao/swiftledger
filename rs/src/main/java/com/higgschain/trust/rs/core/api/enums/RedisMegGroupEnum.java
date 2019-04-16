package com.higgschain.trust.rs.core.api.enums;

import lombok.Getter;

/**
 * The enum Redis meg group enum.
 *
 * @author lingchao
 * @description redis meg group  enum
 * @date 2018 -08-23
 */
@Getter
public enum RedisMegGroupEnum {/**
 * The On persisted callback message notify.
 */
ON_PERSISTED_CALLBACK_MESSAGE_NOTIFY("ON_PERSISTED_CALLBACK_MESSAGE_NOTIFY","on persisted callback message notify"),
    /**
     * The On cluster persisted callback message notify.
     */
    ON_CLUSTER_PERSISTED_CALLBACK_MESSAGE_NOTIFY("ON_CLUSTER_PERSISTED_CALLBACK_MESSAGE_NOTIFY","on persisted callback message notify"),
    ;
    private String code;
    private String desc;

    RedisMegGroupEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
