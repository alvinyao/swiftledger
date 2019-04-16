package com.higgschain.trust.slave.model.enums.biz;

/**
 * The enum Pending tx status enum.
 *
 * @author tangfashuang
 * @date 2018 /04/09 17:45
 * @desc pending transaction status
 */
public enum PendingTxStatusEnum {/**
 * The Init.
 */
INIT("INIT", "master received transaction"),
    /**
     * The Packaged.
     */
    PACKAGED("PACKAGED", "master put transaction to package");

    PendingTxStatusEnum(String code, String desc) {
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
    public static PendingTxStatusEnum getByCode(String code) {
        for (PendingTxStatusEnum enumeration : values()) {
            if (enumeration.getCode().equals(code)) {
                return enumeration;
            }
        }
        return null;
    }
}
