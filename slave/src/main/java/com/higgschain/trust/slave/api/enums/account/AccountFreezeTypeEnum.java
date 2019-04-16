package com.higgschain.trust.slave.api.enums.account;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Account freeze type enum.
 *
 * @author liuyu
 * @description account freeze type
 * @date 2018 -03-30
 */
public enum AccountFreezeTypeEnum {/**
 * The Freeze.
 */
FREEZE("FREEZE", "freeze balance"),
    /**
     * The Unfreeze.
     */
    UNFREEZE("UNFREEZE", "unfreeze balance");

    private String code;
    private String desc;

    AccountFreezeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets bycode.
     *
     * @param code the code
     * @return the bycode
     */
    public static AccountFreezeTypeEnum getBycode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (AccountFreezeTypeEnum item : values()) {
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
