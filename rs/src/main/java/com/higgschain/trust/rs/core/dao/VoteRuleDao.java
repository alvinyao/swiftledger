package com.higgschain.trust.rs.core.dao;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.rs.core.dao.po.VoteRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper public interface VoteRuleDao extends BaseDao<VoteRulePO> {

    /**
     * query vote rule by policy id
     *
     * @param policyId
     * @return
     */
    VoteRulePO queryByPolicyId(@Param("policyId") String policyId);
}
