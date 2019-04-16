package com.higgschain.trust.rs.core.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Vote result enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-06
 */
public enum VoteResultEnum {/**
 * The Init.
 */
INIT("INIT", "INIT status for vote request"),
    /**
     * The Agree.
     */
    AGREE("AGREE", "AGREE status for vote"),
    /**
     * The Disagree.
     */
    DISAGREE("DISAGREE", "DISAGREE status for vote");

    private String code;
    private String desc;

    VoteResultEnum(String code, String desc) {
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
     * From code vote result enum.
     *
     * @param code the code
     * @return the vote result enum
     */
    public static VoteResultEnum fromCode(String code) {
        for (VoteResultEnum patternEnum : values()) {
            if (StringUtils.equals(code, patternEnum.getCode())) {
                return patternEnum;
            }
        }
        return null;
    }
}
