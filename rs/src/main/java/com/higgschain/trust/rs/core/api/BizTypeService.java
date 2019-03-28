package com.higgschain.trust.rs.core.api;

public interface BizTypeService {

    /***
     * get bizType by policyId
     *
     * @param policyId
     * @return
     */
    String getByPolicyId(String policyId);
}
