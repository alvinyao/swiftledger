package com.higgschain.trust.slave.core.service.snapshot.agent;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.core.service.snapshot.SnapshotService;
import com.higgschain.trust.slave.model.bo.DataIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Data identity snapshot agent test.
 */
public class DataIdentitySnapshotAgentTest extends BaseTest{
    @Autowired
    private DataIdentitySnapshotAgent dataIdentitySnapshotAgent;
    @Autowired
    private SnapshotService snapshotService;

    /**
     * Test get data identity.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetDataIdentity() throws Exception {
        System.out.println("testGetDataIdentity:"+ dataIdentitySnapshotAgent.getDataIdentity("12312312"));
    }

    /**
     * Test save data identity.
     *
     * @throws Exception the exception
     */
    @Test
    public void testSaveDataIdentity() throws Exception {
        DataIdentity dataIdentity =  new DataIdentity();
        dataIdentity.setChainOwner("lingchao");
        dataIdentity.setDataOwner("lingchao");
        dataIdentity.setIdentity("lingchao");
        snapshotService.startTransaction();
        dataIdentitySnapshotAgent.saveDataIdentity(dataIdentity);
        snapshotService.commit();
        snapshotService.destroy();
        System.out.println("GetDataIdentity :" + dataIdentitySnapshotAgent.getDataIdentity(dataIdentity.getIdentity()));
        snapshotService.destroy();
    }

    /**
     * Test query.
     *
     * @throws Exception the exception
     */
    @Test
    public void testQuery() throws Exception {
    }

}