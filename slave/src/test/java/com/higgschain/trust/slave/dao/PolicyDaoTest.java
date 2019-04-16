package com.higgschain.trust.slave.dao;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.slave.IntegrateBaseTest;
import com.higgschain.trust.slave.core.repository.PolicyRepository;
import com.higgschain.trust.slave.dao.mysql.manage.PolicyDao;
import com.higgschain.trust.slave.dao.po.manage.PolicyPO;
import com.higgschain.trust.slave.model.bo.manage.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Policy dao test.
 *
 * @author tangfashuang
 * @date 2018 /04/13 18:25
 * @desc policy dao test
 */
public class PolicyDaoTest extends IntegrateBaseTest {

    /**
     * The Policy dao.
     */
    @Autowired PolicyDao policyDao;

    /**
     * The Policy repository.
     */
    @Autowired PolicyRepository policyRepository;

    /**
     * Query by policy id.
     */
    @Test public void queryByPolicyId() {
        Policy policy = policyRepository.getPolicyById("000000");
        Assert.assertEquals("[ALL]", JSON.toJSONString(policy.getRsIds()));
    }

    /**
     * Test add 1.
     */
    @Test public void testAdd1() {
        PolicyPO policy = new PolicyPO();
        policy.setPolicyId("000000");
        policy.setPolicyName("register");
        policy.setRsIds("[\"ALL\"]");

        policyDao.add(policy);
    }

    /**
     * Test add 2.
     */
    @Test public void testAdd2() {
        PolicyPO policy = new PolicyPO();
        policy.setPolicyId("policy-1hsdh6310-23hhs");
        policy.setPolicyName("register");
        List<String> rsIdList = new ArrayList<>();
        rsIdList.add("rs-test1");
        rsIdList.add("rs-test3");
        policy.setRsIds(JSON.toJSONString(rsIdList));

        policyDao.add(policy);
    }


}