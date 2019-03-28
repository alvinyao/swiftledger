package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.IntegrateBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class RsBlockChainServiceTest extends IntegrateBaseTest {
    @Autowired
    private RsBlockChainService rsBlockChainService;

    @Test
    public void testQueryChainOwner() throws Exception {
        System.out.println("chainOwner:" + rsBlockChainService.queryChainOwner());
    }

}