package com.higgschain.trust.slave.dao.mysql.manage;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.manage.PolicyPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Policy dao.
 *
 * @author tangfashuang
 * @date 2018 /03/28
 * @desc policy dao
 */
@Mapper public interface PolicyDao extends BaseDao<PolicyPO> {
    /**
     * query policy by policyId
     *
     * @param policyId the policy id
     * @return policy po
     */
    PolicyPO queryByPolicyId(String policyId);

    /**
     * batch insert
     *
     * @param policyPOList the policy po list
     * @return int
     */
    int batchInsert(List<PolicyPO> policyPOList);
}
