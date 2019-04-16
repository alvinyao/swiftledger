package com.higgschain.trust.slave.model.bo.merkle;

import com.higgschain.trust.slave.api.enums.MerkleStatusEnum;
import com.higgschain.trust.slave.api.enums.MerkleTypeEnum;
import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Merkle node.
 *
 * @author WangQuanzhou
 * @desc Merkle Node BO
 * @date 2018 /4/10 14:27
 */
@Getter @Setter
public class MerkleNode extends BaseBO {
    // merkle node hash
    private String nodeHash;

    // the unique id of merkle node
    private String uuid;

    // the index in the current level
    private long index;

    // current level
    private int level;

    // current level
    private String parent;

    // type of merkle tree，can be ACCOUNT or TX or CONTRACT
    private MerkleTypeEnum treeType;

    // does this merkle node have changed
    // can be NO_CHANGE or ADD or MODIFY, default NO_CHANGE
    private MerkleStatusEnum status = MerkleStatusEnum.NO_CHANGE;

    /**
     * Instantiates a new Merkle node.
     */
    // default constructor
    public MerkleNode() {
    }

    /**
     * Instantiates a new Merkle node.
     *
     * @param nodeHash the node hash
     * @param uuid     the uuid
     * @param index    the index
     * @param level    the level
     * @param parent   the parent
     * @param treeType the tree type
     * @param status   the status
     */
    // constructor
    public MerkleNode(String nodeHash, String uuid, long index, int level, String parent, MerkleTypeEnum treeType,
        MerkleStatusEnum status) {
        this.nodeHash = nodeHash;
        this.uuid = uuid;
        this.index = index;
        this.level = level;
        this.parent = parent;
        this.treeType = treeType;
        this.status = status;
    }
}
