package com.higgschain.trust.rs.core.api.enums;

import lombok.Getter;

/**
 * The enum Redis topic enum.
 *
 * @author lingchao
 * @description redis topic enum
 * @date 2018 -08-23
 */
@Getter
public enum RedisTopicEnum {/**
 * The Async to process init tx.
 */
ASYNC_TO_PROCESS_INIT_TX("ASYNC_TO_PROCESS_INIT_TX", "async to process init tx topic"),
    /**
     * The Callback message notify.
     */
    CALLBACK_MESSAGE_NOTIFY("CALLBACK_MESSAGE_NOTIFY","callback message notify"),
    ;
    private String code;
    private String desc;

    RedisTopicEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }


}
