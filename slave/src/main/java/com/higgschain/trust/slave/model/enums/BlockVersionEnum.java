package com.higgschain.trust.slave.model.enums;

/**
 * The enum Block version enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-12
 */
public enum BlockVersionEnum {/**
 * V 1 block version enum.
 */
V1("v1.0");
    private String code;
    BlockVersionEnum(String code){
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
