package com.higgschain.trust.slave.model.enums;

/**
 * The enum Block header type enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-12
 */
public enum BlockHeaderTypeEnum {/**
 * Consensus validate type block header type enum.
 */
CONSENSUS_VALIDATE_TYPE("CONSENSUS_VALIDATE_TYPE"),
    /**
     * Consensus persist type block header type enum.
     */
    CONSENSUS_PERSIST_TYPE("CONSENSUS_PERSIST_TYPE"),
    /**
     * Temp type block header type enum.
     */
    TEMP_TYPE("TEMP_TYPE");
    private String code;
    BlockHeaderTypeEnum(String code){
        this.code = code;
    }

    /**
     * Get code string.
     *
     * @return the string
     */
    public String getCode(){
        return this.code;
    }
}
