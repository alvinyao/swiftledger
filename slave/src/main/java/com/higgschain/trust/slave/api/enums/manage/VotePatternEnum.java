package com.higgschain.trust.slave.api.enums.manage;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Vote pattern enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-06
 */
public enum VotePatternEnum {/**
 * The Sync.
 */
SYNC("SYNC", "vote by sync pattern"),
    /**
     * The Async.
     */
    ASYNC("ASYNC", "vote by async pattern");

    private String code;
    private String desc;

    VotePatternEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
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

    /**
     * From code vote pattern enum.
     *
     * @param code the code
     * @return the vote pattern enum
     */
    public static VotePatternEnum fromCode(String code) {
        for (VotePatternEnum patternEnum : values()) {
            if (StringUtils.equals(code, patternEnum.getCode())) {
                return patternEnum;
            }
        }
        return null;
    }
}
