package com.higgschain.trust.slave.model.enums;

/**
 * The enum Usage enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-12
 */
public enum UsageEnum {/**
 * Biz usage enum.
 */
BIZ("biz"),
    /**
     * Consensus usage enum.
     */
    CONSENSUS("consensus"),;
    private String code;

    UsageEnum(String code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return this.code;
    }
}
