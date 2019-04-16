package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.IntegrateBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Rs block chain service test.
 */
public class RsBlockChainServiceTest extends IntegrateBaseTest {
    @Autowired
    private RsBlockChainService rsBlockChainService;

    /**
     * Test query chain owner.
     *
     * @throws Exception the exception
     */
    @Test
    public void testQueryChainOwner() throws Exception {
        System.out.println("chainOwner:" + rsBlockChainService.queryChainOwner());
    }

}