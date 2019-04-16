package com.higgschain.trust.slave.core.repository;

import com.higgschain.trust.slave.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Data identity repository test.
 */
public class DataIdentityRepositoryTest extends BaseTest {
    @Autowired
    private DataIdentityRepository dataIdentityRepository;

    /**
     * Test query data identity.
     *
     * @throws Exception the exception
     */
    @Test
    public void testQueryDataIdentity() throws Exception {
        System.out.println("queryByIdentity:" + dataIdentityRepository.queryDataIdentity("12312312"));
    }

}