package com.higgschain.trust.slave.model.enums.biz;

/**
 * The enum Rs node status enum.
 */
public enum RsNodeStatusEnum {/**
 * The Common.
 */
COMMON("COMMON","rs node register"),
    /**
     * The Canceled.
     */
    CANCELED("CANCELED", "rs node canceled");

    RsNodeStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
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
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets by code.
     *
     * @param code the code
     * @return the by code
     */
    public static RsNodeStatusEnum getByCode(String code) {
        for (RsNodeStatusEnum enumeration : values()) {
            if (enumeration.getCode().equals(code)) {
                return enumeration;
            }
        }
        return null;
    }
}
