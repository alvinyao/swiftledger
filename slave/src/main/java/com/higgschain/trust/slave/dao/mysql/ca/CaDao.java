package com.higgschain.trust.slave.dao.mysql.ca;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.ca.CaPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Ca dao.
 *
 * @author WangQuanzhou
 * @desc CA dao
 * @date 2018 /6/5 10:18
 */
@Mapper public interface CaDao extends BaseDao {

    /**
     * Insert ca.
     *
     * @param caPO the ca po
     * @return
     * @desc insert CA into db
     */
    void insertCa(CaPO caPO);

    /**
     * Update ca.
     *
     * @param caPO the ca po
     * @return
     * @desc update CA information
     */
    void updateCa(CaPO caPO);

    /**
     * Gets ca for biz.
     *
     * @param user the user
     * @return CaPO ca for biz
     * @desc get CA information by nodeName
     */
    CaPO getCaForBiz(String user);

    /**
     * Gets all ca.
     *
     * @param
     * @return all ca
     * @desc get all CA information
     */
    List<CaPO> getAllCa();

    /**
     * batch insert
     *
     * @param caPOList the ca po list
     * @return int
     */
    int batchInsert(List<CaPO> caPOList);

    /**
     * batch update
     *
     * @param caPOList the ca po list
     * @return int
     */
    int batchUpdate(List<CaPO> caPOList);

    /**
     * Gets ca for consensus.
     *
     * @param user the user
     * @return ca for consensus
     * @desc get ca info for consensus layer
     */
    CaPO getCaForConsensus(String user);

    /**
     * get all pubKeys
     *
     * @param usage the usage
     * @return all pubkey by usage
     */
    List<CaPO> getAllPubkeyByUsage(@Param("usage")String usage);
}
