package com.higgschain.trust.slave.core.service.consensus.p2p;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class P2pHandlerTest extends BaseTest {
    @Autowired
    P2pHandler p2pHandler;

    @Test public void testSendValidating() throws Exception {
        BlockHeader header = new BlockHeader();
        p2pHandler.sendPersisting(header);
    }

}