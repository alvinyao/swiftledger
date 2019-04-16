package com.higgschain.trust.rs.core.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Core tx result enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-12
 */
public enum CoreTxResultEnum {/**
 * The Success.
 */
SUCCESS("SUCCESS","the tx execute success"),
    /**
     * The Fail.
     */
    FAIL("FAIL","the tx execute fail");

    private String code;
    private String desc;

    CoreTxResultEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    /**
     * Form code core tx result enum.
     *
     * @param code the code
     * @return the core tx result enum
     */
    public static CoreTxResultEnum formCode(String code){
        for(CoreTxResultEnum coreTxStatusEnum : values()){
            if(StringUtils.equals(code,coreTxStatusEnum.getCode())){
                return coreTxStatusEnum;
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
