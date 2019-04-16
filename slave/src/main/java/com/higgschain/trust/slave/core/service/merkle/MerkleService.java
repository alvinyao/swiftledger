package com.higgschain.trust.slave.core.service.merkle;

import com.higgschain.trust.slave.api.enums.MerkleTypeEnum;
import com.higgschain.trust.slave.model.bo.merkle.MerkleTree;

import java.util.List;

/**
 * The interface Merkle service.
 *
 * @author WangQuanzhou
 * @desc merkle tree service interface
 * @date 2018 /4/10 16:28
 */
public interface MerkleService {
    /**
     * create a merkle tree
     *
     * @param type     the type
     * @param dataList the data list
     * @return merkle tree
     */
    MerkleTree build(MerkleTypeEnum type, List<Object> dataList);

    /**
     * is exist
     *
     * @param merkleTree the merkle tree
     * @param obj        the obj
     * @return boolean
     */
    boolean isExist(MerkleTree merkleTree,Object obj);

    /**
     * update a merkle tree
     *
     * @param merkleTree the merkle tree
     * @param objOld     the obj old
     * @param objNew     the obj new
     */
    void update(MerkleTree merkleTree, Object objOld, Object objNew);

    /**
     * insert one node into a merkle tree
     *
     * @param merkleTree the merkle tree
     * @param obj        the obj
     */
    void add(MerkleTree merkleTree, Object obj);

    /**
     * query a merkle tree with the exact type
     *
     * @param treeType the tree type
     * @return merkle tree
     */
    MerkleTree queryMerkleTree(MerkleTypeEnum treeType);

}
