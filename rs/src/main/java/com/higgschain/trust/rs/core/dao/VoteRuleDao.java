package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.VoteRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * The interface Vote rule dao.
 */
@Mapper public interface VoteRuleDao extends BaseDao<VoteRulePO> {

    /**
     * query vote rule by policy id
     *
     * @param policyId the policy id
     * @return vote rule po
     */
    VoteRulePO queryByPolicyId(@Param("policyId") String policyId);
}
