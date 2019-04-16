package com.higgschain.trust.rs.common.enums;

import lombok.Getter;

/**
 * 请求状态枚举
 *
 * @author lingchao
 */
@Getter
public enum RequestEnum {/**
 * Process request enum.
 */
PROCESS("PROCESS", "正在处理"),
    /**
     * Done request enum.
     */
    DONE("DONE", "处理完成");

    RequestEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }
    private String code;

    private String desc;

}
