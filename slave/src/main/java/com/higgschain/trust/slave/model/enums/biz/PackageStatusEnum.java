package com.higgschain.trust.slave.model.enums.biz;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Package status enum.
 *
 * @author tangfashuang
 * @date 2018 /04/09 17:48
 * @desc package status
 */
public enum PackageStatusEnum {/**
 * The Received.
 */
//@formatter:off
    RECEIVED("01", "RECEIVED", "package received from consensus"),
    /**
     * The Wait persist consensus.
     */
    WAIT_PERSIST_CONSENSUS("02", "WAIT_PERSIST_CONSENSUS","self persisting end"),
    /**
     * The Persisted.
     */
    PERSISTED("03", "PERSISTED", "package complete persist"),
    /**
     * The Failover.
     */
    FAILOVER("04", "FAILOVER","failover package");
    //@formatter:on

    PackageStatusEnum(String index, String code, String desc) {
        this.index = index;
        this.code = code;
        this.desc = desc;
    }

    private String index;

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
    public static PackageStatusEnum getByCode(String code) {
        for (PackageStatusEnum enumeration : values()) {
            if (enumeration.getCode().equals(code)) {
                return enumeration;
            }
        }
        return null;
    }

    /**
     * Gets by index.
     *
     * @param index the index
     * @return the by index
     */
    public static PackageStatusEnum getByIndex(String index) {
        for (PackageStatusEnum enumeration : values()) {
            if (enumeration.getIndex().equals(index)) {
                return enumeration;
            }
        }
        return null;
    }

    /**
     * Gets indexs.
     *
     * @param index the index
     * @return the indexs
     */
    public static List<String> getIndexs(String index) {
        List<String> indexList = new ArrayList<>();
        if (StringUtils.isEmpty(index)) {
            for (PackageStatusEnum enumeration : values()) {
                indexList.add(enumeration.getIndex());
            }
        } else {
            for (PackageStatusEnum enumeration : values()) {
                if (enumeration.getIndex().compareTo(index) > 0) {
                    indexList.add(enumeration.getIndex());
                }
            }
        }
        return indexList;
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
