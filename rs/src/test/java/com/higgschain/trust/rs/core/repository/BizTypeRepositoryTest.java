package com.higgschain.trust.rs.core.repository;

import com.higgschain.trust.IntegrateBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The type Biz type repository test.
 */
public class BizTypeRepositoryTest extends IntegrateBaseTest {

    @Autowired
    private BizTypeRepository bizTypeRepository;

    /**
     * Test add.
     */
    @Test public void testAdd() {
        Assert.assertEquals(
            bizTypeRepository.add("test-policy", "register cass"), "add biz type success");
    }

    /**
     * Test get by policy id.
     *
     * @throws Exception the exception
     */
    @Test public void testGetByPolicyId() throws Exception {
        bizTypeRepository.getByPolicyId("test-policy");
    }
}