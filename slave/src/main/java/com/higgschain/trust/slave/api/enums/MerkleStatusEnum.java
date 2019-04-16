package com.higgschain.trust.slave.api.enums;

/**
 * The enum Merkle status enum.
 *
 * @author WangQuanzhou
 * @desc merkle node operate type enum
 * @date 2018 /3/27 11:55
 */
public enum MerkleStatusEnum {/**
 * The No change.
 */
NO_CHANGE("NO_CHANGE", "no change"),
    /**
     * Add merkle status enum.
     */
    ADD("ADD", "add"),
    /**
     * Modify merkle status enum.
     */
    MODIFY("MODIFY", "modify"), ;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    MerkleStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets biz type enum bycode.
     *
     * @param code the code
     * @return the biz type enum bycode
     */
    public static MerkleStatusEnum getBizTypeEnumBycode(String code) {
        for (MerkleStatusEnum versionEnum : MerkleStatusEnum.values()) {
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
