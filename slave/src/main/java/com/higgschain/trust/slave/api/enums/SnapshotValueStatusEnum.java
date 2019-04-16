package com.higgschain.trust.slave.api.enums;

import lombok.Getter;

/**
 * Snapshot value status enum
 *
 * @author lingchao
 * @create 2018年04月09日16 :28
 */
@Getter
public enum SnapshotValueStatusEnum {/**
 * The Insert.
 */
INSERT("INSERT", "insert data for Snapshot"),
    /**
     * The Update.
     */
    UPDATE("UPDATE", "update data for Snapshot");

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    SnapshotValueStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
