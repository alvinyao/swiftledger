package com.higgschain.trust.slave.dao.po.merkle;

import com.higgschain.trust.common.mybatis.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Merkle tree po.
 *
 * @author WangQuanzhou
 * @desc merkle tree po
 * @date 2018 /3/29 16:30
 */
@Getter @Setter public class MerkleTreePO extends BaseEntity {

    private static final long serialVersionUID = -8977162340234152348L;
    // Merkle Root
    private String rootHash;

    // the total level of the merkle tree
    private int totalLevel;

    // the max index of leaf level of the merkle tree
    private Long maxIndex;

    // type of merkle tree，can be ACCOUNT or TX or CONTRACT
    private String treeType;

    // create time
    private Date createTime;

    // update time
    private Date updateTime;

    /**
     * Instantiates a new Merkle tree po.
     */
    public MerkleTreePO() {
    }

    /**
     * Instantiates a new Merkle tree po.
     *
     * @param rootHash   the root hash
     * @param totalLevel the total level
     * @param maxIndex   the max index
     * @param treeType   the tree type
     */
    public MerkleTreePO(String rootHash, int totalLevel, Long maxIndex, String treeType) {
        this.rootHash = rootHash;
        this.totalLevel = totalLevel;
        this.maxIndex = maxIndex;
        this.treeType = treeType;
    }
}
