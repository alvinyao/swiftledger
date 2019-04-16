package com.higgschain.trust.slave.api.enums.account;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Change direction enum.
 *
 * @author liuyu
 * @description the account balance change direction
 * @date 2018 -03-28
 */
public enum ChangeDirectionEnum {/**
 * The Increase.
 */
INCREASE("INCREASE", "increase in balance"),
    /**
     * The Decrease.
     */
    DECREASE("DECREASE", "decrease in balance");

    private String code;
    private String desc;

    ChangeDirectionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets bycode.
     *
     * @param code the code
     * @return the bycode
     */
    public static ChangeDirectionEnum getBycode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (ChangeDirectionEnum item : values()) {
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
