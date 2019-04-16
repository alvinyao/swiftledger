package com.higgschain.trust.slave.api.enums.account;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Fund direction enum.
 *
 * @author liuyu
 * @description fund direction DEBIT or CREDIT
 * @date 2018 -03-27
 */
public enum FundDirectionEnum {/**
 * The Debit.
 */
DEBIT("DEBIT", "debit balance"),
    /**
     * The Credit.
     */
    CREDIT("CREDIT", "credit balance");

    private String code;
    private String desc;

    FundDirectionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets bycode.
     *
     * @param code the code
     * @return the bycode
     */
    public static FundDirectionEnum getBycode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (FundDirectionEnum item : values()) {
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
