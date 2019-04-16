package com.higgschain.trust.slave.core.service.consensus.log;

import com.google.common.collect.Lists;
import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.consensus.PackageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Log replicate handler impl test.
 */
public class LogReplicateHandlerImplTest extends BaseTest {
    /**
     * The Log replicate handler.
     */
    @Autowired
    LogReplicateHandler logReplicateHandler;

    /**
     * Test replicate package.
     *
     * @throws Exception the exception
     */
    @Test public void testReplicatePackage() throws Exception {
        List<Package> packageVOList = new LinkedList<>();
        Package pack = new Package();
        pack.setHeight(10L);
        pack.setSignedTxList(Lists.newArrayList());

        packageVOList.add(pack);
        logReplicateHandler.replicatePackage(new PackageCommand(null,pack));
    }

}