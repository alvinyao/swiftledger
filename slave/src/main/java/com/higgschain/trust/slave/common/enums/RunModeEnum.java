package com.higgschain.trust.slave.common.enums;

/**
 * The enum Run mode enum.
 */
public enum RunModeEnum {/**
 * Cluster run mode enum.
 */
CLUSTER("cluster", "集群模式启动"),
    /**
     * Single run mode enum.
     */
    SINGLE("single", "单节点动态加入"),;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    RunModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets biz type enum bycode.
     *
     * @param code the code
     * @return the biz type enum bycode
     */
    public static RunModeEnum getBizTypeEnumBycode(String code) {
        for (RunModeEnum versionEnum : RunModeEnum.values()) {
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
