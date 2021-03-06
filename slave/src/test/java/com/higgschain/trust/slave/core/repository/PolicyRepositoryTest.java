package com.higgschain.trust.slave.core.repository;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.api.enums.manage.DecisionTypeEnum;
import com.higgschain.trust.slave.dao.po.manage.PolicyPO;
import com.higgschain.trust.slave.model.bo.manage.Policy;
import com.higgschain.trust.slave.model.bo.manage.RegisterPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * The type Policy repository test.
 */
/*
 *
 * @desc
 * @author tangfashuang
 * @date 2018/4/14
 *
 */
public class PolicyRepositoryTest extends BaseTest {

    @Autowired
    private PolicyRepository policyRepository;

    private Policy policy;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @BeforeMethod public void setUp() throws Exception {
        List<String> rsIds = new ArrayList<>();
        rsIds.add("rs-test1");
        rsIds.add("rs-test3");
        rsIds.add("rs-test3");
        policy = new Policy();
        policy.setPolicyId("policy-test-1");
        policy.setPolicyName("注册policy-test-1");
        policy.setDecisionType(DecisionTypeEnum.FULL_VOTE);
        policy.setContractAddr(null);
        policy.setRsIds(rsIds);
    }

    /**
     * Gets policy by id.
     */
    @Test public void getPolicyById() {
        Policy policy = policyRepository.getPolicyById("policy-test-1");
//        assertEquals(null, policy);
        System.out.println(policy);
    }

    /**
     * Convert action to policy.
     */
    @Test public void convertActionToPolicy() {
        RegisterPolicy registerPolicy = new RegisterPolicy();
        registerPolicy.setPolicyId("test-0000");
        registerPolicy.setPolicyName("测试test");
        List<String> rsIds = new ArrayList<>();
        rsIds.add("rs-test1");
        rsIds.add("rs-test2");
        rsIds.add("rs-test2");
        registerPolicy.setRsIds(rsIds);

        Policy policy = policyRepository.convertActionToPolicy(registerPolicy);

        assertEquals(policy.getPolicyId(), registerPolicy.getPolicyId());
        assertEquals(policy.getPolicyName(), registerPolicy.getPolicyName());
        assertEquals(policy.getRsIds(), registerPolicy.getRsIds());
    }

    /**
     * Gets policy type.
     */
    @Test
    public void getPolicyType() {
        String type = policyRepository.getPolicyType("000001");
        assertEquals("REGISTER_POLICY", type);
    }

    /**
     * Batch insert.
     */
    @Test
    public void batchInsert() {
        List<PolicyPO> policyPOList = new ArrayList<>();
        policyPOList.add(policyRepository.convertPolicyToPolicyPO(policy));
        int insert = policyRepository.batchInsert(policyPOList);
        assertEquals(insert, 1);
    }
}