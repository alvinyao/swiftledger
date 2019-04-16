package com.higgschain.trust.slave.api.enums.account;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Trade direction enum.
 *
 * @author liuyu
 * @description accounting trade direction DEBIT or CREDIT
 * @date 2018 -03-27
 */
public enum TradeDirectionEnum {/**
 * Debit trade direction enum.
 */
DEBIT("DEBIT", "debit"),
    /**
     * Credit trade direction enum.
     */
    CREDIT("CREDIT", "credit");

    private String code;
    private String desc;

    TradeDirectionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets bycode.
     *
     * @param code the code
     * @return the bycode
     */
    public static TradeDirectionEnum getBycode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (TradeDirectionEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}
