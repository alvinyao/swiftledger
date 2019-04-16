package com.higgschain.trust.slave.api.enums;

import lombok.Getter;

/**
 * Snapshot core key enum
 *
 * @author lingchao
 * @create 2018年04月09日16 :28
 */
@Getter
public enum SnapshotBizKeyEnum {/**
 * The Account.
 */
ACCOUNT("ACCOUNT", "account core Snapshot"),
    /**
     * The Account detail.
     */
    ACCOUNT_DETAIL("ACCOUNT_DETAIL", "account detail Snapshot"),
    /**
     * The Freeze.
     */
    FREEZE("FREEZE", "freeze core Snapshot"),
    /**
     * The Contract.
     */
    CONTRACT("CONTRACT", "contract core Snapshot"),
    /**
     * The Account contract bind.
     */
    ACCOUNT_CONTRACT_BIND("ACCOUNT_CONTRACT_BIND", "account contract binding Snapshot"),
    /**
     * The Contract sate.
     */
    CONTRACT_SATE("CONTRACT_SATE", "contract state Snapshot"),
    /**
     * The Data identity.
     */
    DATA_IDENTITY("DATA_IDENTITY", "dataIdentity core Snapshot"),
    /**
     * The Utxo.
     */
    UTXO("UTXO", "UTXO core Snapshot"),
    /**
     * Manage snapshot biz key enum.
     */
    MANAGE("MANAGE", "manage  Snapshot"),
    /**
     * The Merkle tree.
     */
    MERKLE_TREE("MERKLE_TREE", "merkle tree  Snapshot"),
    /**
     * Other snapshot biz key enum.
     */
    OTHER("OTHER", "other  Snapshot"),
    /**
     * The Ca.
     */
    CA("CA", "ca Snapshot"),;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    SnapshotBizKeyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets by enum.
     *
     * @param snapshotBizKeyEnum the snapshot biz key enum
     * @return the by enum
     */
    public static SnapshotBizKeyEnum getByEnum(SnapshotBizKeyEnum snapshotBizKeyEnum) {
        for (SnapshotBizKeyEnum item : values()) {
            if (snapshotBizKeyEnum == item) {
                return item;
            }
        }
        return null;
    }

}
