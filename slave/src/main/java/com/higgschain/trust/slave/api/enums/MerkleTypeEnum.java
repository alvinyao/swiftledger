package com.higgschain.trust.slave.api.enums;

/**
 * The enum Merkle type enum.
 *
 * @author WangQuanzhou
 * @desc merkle tree type enum
 * @date 2018 /3/27 11:55
 */
public enum MerkleTypeEnum {/**
 * The Account.
 */
ACCOUNT("ACCOUNT", "account type"),
    /**
     * The Contract.
     */
    CONTRACT("CONTRACT", "contract type"),
    /**
     * The Tx.
     */
    TX("TX", "transaction type"),
    /**
     * The Tx receiept.
     */
    TX_RECEIEPT("TX_RECEIEPT", "txReceiept type"),
    /**
     * The Policy.
     */
    POLICY("POLICY", "policy type"),
    /**
     * The Rs.
     */
    RS("RS", "rs type"),
    /**
     * The Ca.
     */
    CA("CA", "CA type"),;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    MerkleTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets biz type enum bycode.
     *
     * @param code the code
     * @return the biz type enum bycode
     */
    public static MerkleTypeEnum getBizTypeEnumBycode(String code) {
        for (MerkleTypeEnum versionEnum : MerkleTypeEnum.values()) {
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
