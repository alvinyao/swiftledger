package com.higgschain.trust.rs.core.api;

/**
 * The interface Biz type service.
 */
public interface BizTypeService {

    /***
     * get bizType by policyId
     *
     * @param policyId the policy id
     * @return by policy id
     */
    String getByPolicyId(String policyId);
}
