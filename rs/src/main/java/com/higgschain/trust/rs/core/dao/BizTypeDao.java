package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.BizTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Biz type dao.
 */
@Mapper public interface BizTypeDao extends BaseDao<BizTypePO> {
    /**
     * query by policy id
     *
     * @param policyId the policy id
     * @return biz type po
     */
    BizTypePO queryByPolicyId(@Param("policyId") String policyId);

    /**
     * query all bizType
     *
     * @return list
     */
    List<BizTypePO> queryAll();

    /**
     * update bizType
     *
     * @param policyId the policy id
     * @param bizType  the biz type
     */
    void update(String policyId, String bizType);
}
