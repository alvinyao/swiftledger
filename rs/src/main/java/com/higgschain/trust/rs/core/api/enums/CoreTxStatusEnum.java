package com.higgschain.trust.rs.core.api.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Core tx status enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-12
 */
public enum CoreTxStatusEnum {
    /**
     * The Init.
     */
    INIT("01", "INIT", "the init status"),
    /**
     * The Need vote.
     */
    NEED_VOTE("02", "NEED_VOTE", "need vote by async"),
    /**
     * The Wait.
     */
    WAIT("03", "WAIT", "wait submit to slave");

    private String index;
    private String code;
    private String desc;

    CoreTxStatusEnum(String index, String code, String desc) {
        this.index = index;
        this.code = code;
        this.desc = desc;
    }

    /**
     * Form code core tx status enum.
     *
     * @param code the code
     * @return the core tx status enum
     */
    public static CoreTxStatusEnum formCode(String code) {
        for (CoreTxStatusEnum coreTxStatusEnum : values()) {
            if (StringUtils.equals(code, coreTxStatusEnum.getCode())) {
                return coreTxStatusEnum;
            }
        }
        return null;
    }

    /**
     * Form index core tx status enum.
     *
     * @param index the index
     * @return the core tx status enum
     */
    public static CoreTxStatusEnum formIndex(String index) {
        for (CoreTxStatusEnum coreTxStatusEnum : values()) {
            if (StringUtils.equals(index, coreTxStatusEnum.getIndex())) {
                return coreTxStatusEnum;
            }
        }
        return null;
    }

    /**
     * Gets index list.
     *
     * @param index the index
     * @return the index list
     */
    public static List<String> getIndexList(String index) {
        List<String> indexList = new ArrayList<>();
        if (!StringUtils.isEmpty(index)) {
            for (CoreTxStatusEnum enumeration : values()) {
                if (enumeration.getIndex().compareTo(index) > 0) {
                    indexList.add(enumeration.getIndex());
                }
            }
        } else {
            for (CoreTxStatusEnum enumeration : values()) {
                indexList.add(enumeration.getIndex());
            }
        }
        return indexList;
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

    /**
     * Gets index.
     *
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     */
    public void setIndex(String index) {
        this.index = index;
    }
}
