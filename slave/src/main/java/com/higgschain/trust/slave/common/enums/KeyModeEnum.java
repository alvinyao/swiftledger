package com.higgschain.trust.slave.common.enums;

/**
 * The enum Key mode enum.
 *
 * @author WangQuanzhou
 * @desc cluster 's keyPair generate mode, auto or manual
 * @date 2018 /8/27 19:15
 */
public enum KeyModeEnum {/**
 * Auto key mode enum.
 */
AUTO("auto", "自动生成公私钥模式"),
    /**
     * Manual key mode enum.
     */
    MANUAL("manual", "手动配置公私钥模式"),;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    KeyModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets biz type enum bycode.
     *
     * @param code the code
     * @return the biz type enum bycode
     */
    public static KeyModeEnum getBizTypeEnumBycode(String code) {
        for (KeyModeEnum versionEnum : KeyModeEnum.values()) {
            if (versionEnum.getCode().equals(code)) {
                return versionEnum;
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
