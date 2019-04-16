package com.higgschain.trust.slave.model.enums.biz;

/**
 * The enum Tx submit result enum.
 *
 * @author tangfashuang
 * @date 2018 /05/12 20:46
 * @desc transaction submit result enum
 */
public enum TxSubmitResultEnum {/**
 * The Param invalid.
 */
PARAM_INVALID("100000", "transaction param invalid"),
    /**
     * The Pending tx idempotent.
     */
    PENDING_TX_IDEMPOTENT("200000", "pending transaction idempotent"),
    /**
     * The Tx idempotent.
     */
    TX_IDEMPOTENT("300000", "transaction idempotent"),
    /**
     * The Node state is not running.
     */
    NODE_STATE_IS_NOT_RUNNING("400000", "node state is not running"),
    /**
     * The Tx queue size too large.
     */
    TX_QUEUE_SIZE_TOO_LARGE("500000", "pending transaction queue size is too large");

    TxSubmitResultEnum(String code, String desc) {
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
    public static TxSubmitResultEnum getByCode(String code) {
        for (TxSubmitResultEnum enumeration : values()) {
            if (enumeration.getCode().equals(code)) {
                return enumeration;
            }
        }
        return null;
    }
}
