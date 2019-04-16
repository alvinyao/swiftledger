package com.higgschain.trust.slave.api.enums.account;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Account state enum.
 *
 * @author liuyu
 * @description account status
 * @date 2018 -03-27
 */
public enum AccountStateEnum {/**
 * The Normal.
 */
NORMAL("NORMAL", "account is normal"),
    /**
     * The Destroy.
     */
    DESTROY("DESTROY", "account is destoryed");

    private String code;
    private String desc;

    AccountStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets bycode.
     *
     * @param code the code
     * @return the bycode
     */
    public static AccountStateEnum getBycode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (AccountStateEnum item : values()) {
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
