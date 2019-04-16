package com.higgschain.trust.consensus.config;

import com.higgschain.trust.consensus.config.NodeProperties;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.config.listener.MasterChangeListener;
import com.higgschain.trust.consensus.exception.ConsensusError;
import com.higgschain.trust.consensus.exception.ConsensusException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * Copyright (c) 2013-2017, suimi
 */

/**
 * The type Node state test.
 */
@RunWith(PowerMockRunner.class) public class NodeStateTest {

    @InjectMocks private NodeState nodeState;

    @Mock private NodeProperties properties;

    private String nodeName = "prefix_nodeName";
    private String prefix = "prefix";

    /**
     * Before method.
     */
    @BeforeMethod public void beforeMethod() {
        nodeState = new NodeState();
        MockitoAnnotations.initMocks(this);
        Mockito.when(properties.getNodeName()).thenReturn(nodeName);
        Mockito.when(properties.getPrefix()).thenReturn(prefix);
        nodeState.afterPropertiesSet();
    }

    /**
     * Test register master listener.
     */
    @Test public void testRegisterMasterListener() {
        MasterChangeListener masterChangeListener = Mockito.mock(MasterChangeListener.class);
        nodeState.registerMasterListener(masterChangeListener);
        String masterName = "masterName";
        nodeState.changeMaster(masterName);
        Mockito.verify(masterChangeListener, Mockito.times(1)).masterChanged(masterName);
    }

    /**
     * Test change master.
     */
    @Test public void testChangeMaster() {
        String newMasterName = "newMasterName";
        nodeState.changeMaster(newMasterName);
        Assert.assertEquals(nodeState.getMasterName(), newMasterName);
        Assert.assertFalse(nodeState.isMaster());

        nodeState.changeMaster(nodeName);
        Assert.assertEquals(nodeState.getMasterName(), nodeName);
        Assert.assertTrue(nodeState.isMaster());

    }

    /**
     * Starting object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] starting() {
        return new Object[][] {new Object[] {NodeStateEnum.SelfChecking},};
    }

    /**
     * Starting exception object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] startingException() {
        return new Object[][] {new Object[] {NodeStateEnum.Starting},
            //            new Object[]{NodeStateEnum.SelfChecking},
            new Object[] {NodeStateEnum.AutoSync}, new Object[] {NodeStateEnum.ArtificialSync},
            new Object[] {NodeStateEnum.Running}, new Object[] {NodeStateEnum.Offline},};
    }

    /**
     * Test change state starting.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "starting") public void testChangeStateStarting(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, toState);
        Assert.assertEquals(nodeState.getState(), toState);
    }

    /**
     * Test change state starting exception.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "startingException") public void testChangeStateStartingException(NodeStateEnum toState) {
        try {
            nodeState.changeState(NodeStateEnum.Starting, toState);
        } catch (ConsensusException e) {
            Assert.assertEquals(e.getCode(), ConsensusError.CONFIG_NODE_STATE_CHANGE_FAILED);
        }
        Assert.assertEquals(nodeState.getState(), NodeStateEnum.Starting);
    }

    /**
     * Self object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] self() {
        return new Object[][] {new Object[] {NodeStateEnum.AutoSync}, new Object[] {NodeStateEnum.ArtificialSync},
            new Object[] {NodeStateEnum.Running}, new Object[] {NodeStateEnum.Offline},};
    }

    /**
     * Self exception object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] selfException() {
        return new Object[][] {new Object[] {NodeStateEnum.Starting}, new Object[] {NodeStateEnum.SelfChecking},};
    }

    /**
     * Test change state self.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "self") public void testChangeStateSelf(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, toState);
        Assert.assertEquals(nodeState.getState(), toState);
    }

    /**
     * Test change state self exception.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "selfException") public void testChangeStateSelfException(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        try {
            nodeState.changeState(NodeStateEnum.SelfChecking, toState);
        } catch (ConsensusException e) {
            Assert.assertEquals(e.getCode(), ConsensusError.CONFIG_NODE_STATE_CHANGE_FAILED);
        }
        Assert.assertEquals(nodeState.getState(), NodeStateEnum.SelfChecking);
    }

    /**
     * Sync object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] sync() {
        return new Object[][] {new Object[] {NodeStateEnum.SelfChecking}, new Object[] {NodeStateEnum.Running},
            new Object[] {NodeStateEnum.Offline}};
    }

    /**
     * Sync exception object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] syncException() {
        return new Object[][] {new Object[] {NodeStateEnum.Starting}, new Object[] {NodeStateEnum.AutoSync},
            new Object[] {NodeStateEnum.ArtificialSync}};
    }

    /**
     * Test change state auto sync.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "sync") public void testChangeStateAutoSync(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.AutoSync);
        nodeState.changeState(NodeStateEnum.AutoSync, toState);
        Assert.assertEquals(nodeState.getState(), toState);
    }

    /**
     * Test change state auto sync exception.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "syncException") public void testChangeStateAutoSyncException(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.AutoSync);
        try {
            nodeState.changeState(NodeStateEnum.AutoSync, toState);
        } catch (ConsensusException e) {
            Assert.assertEquals(e.getCode(), ConsensusError.CONFIG_NODE_STATE_CHANGE_FAILED);
        }
        Assert.assertEquals(nodeState.getState(), NodeStateEnum.AutoSync);
    }

    /**
     * Test change state artificial sync.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "sync") public void testChangeStateArtificialSync(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.ArtificialSync);
        nodeState.changeState(NodeStateEnum.ArtificialSync, toState);
        Assert.assertEquals(nodeState.getState(), toState);
    }

    /**
     * Test change state artificial sync exception.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "syncException") public void testChangeStateArtificialSyncException(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.ArtificialSync);
        try {
            nodeState.changeState(NodeStateEnum.ArtificialSync, toState);
        } catch (ConsensusException e) {
            Assert.assertEquals(e.getCode(), ConsensusError.CONFIG_NODE_STATE_CHANGE_FAILED);
        }
        Assert.assertEquals(nodeState.getState(), NodeStateEnum.ArtificialSync);
    }

    /**
     * Running object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] running() {
        return new Object[][] {new Object[] {NodeStateEnum.SelfChecking}, new Object[] {NodeStateEnum.Offline}};
    }

    /**
     * Running exception object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] runningException() {
        return new Object[][] {new Object[] {NodeStateEnum.Starting}, new Object[] {NodeStateEnum.AutoSync},
            new Object[] {NodeStateEnum.ArtificialSync}, new Object[] {NodeStateEnum.Running},};
    }

    /**
     * Test change state running.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "running") public void testChangeStateRunning(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.Running);
        nodeState.changeState(NodeStateEnum.Running, toState);
        Assert.assertEquals(nodeState.getState(), toState);
    }

    /**
     * Test change state running exception.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "runningException") public void testChangeStateRunningException(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.Running);
        try {
            nodeState.changeState(NodeStateEnum.Running, toState);
        } catch (ConsensusException e) {
            Assert.assertEquals(e.getCode(), ConsensusError.CONFIG_NODE_STATE_CHANGE_FAILED);
        }
        Assert.assertEquals(nodeState.getState(), NodeStateEnum.Running);
    }

    /**
     * Offline object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] offline() {
        return new Object[][] {new Object[] {NodeStateEnum.SelfChecking},};
    }

    /**
     * Offline exception object [ ] [ ].
     *
     * @return the object [ ] [ ]
     */
    @DataProvider public Object[][] offlineException() {
        return new Object[][] {new Object[] {NodeStateEnum.Starting}, new Object[] {NodeStateEnum.AutoSync},
            new Object[] {NodeStateEnum.ArtificialSync}, new Object[] {NodeStateEnum.Running},
            new Object[] {NodeStateEnum.Offline},};
    }

    /**
     * Test change state offline.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "offline") public void testChangeStateOffline(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.Offline);
        nodeState.changeState(NodeStateEnum.Offline, toState);
        Assert.assertEquals(nodeState.getState(), toState);
    }

    /**
     * Test change state offline exception.
     *
     * @param toState the to state
     */
    @Test(dataProvider = "offlineException") public void testChangeStateOfflineException(NodeStateEnum toState) {
        nodeState.changeState(NodeStateEnum.Starting, NodeStateEnum.SelfChecking);
        nodeState.changeState(NodeStateEnum.SelfChecking, NodeStateEnum.Offline);
        try {
            nodeState.changeState(NodeStateEnum.Offline, toState);
        } catch (ConsensusException e) {
            Assert.assertEquals(e.getCode(), ConsensusError.CONFIG_NODE_STATE_CHANGE_FAILED);
        }
        Assert.assertEquals(nodeState.getState(), NodeStateEnum.Offline);
    }

    /**
     * Test is state.
     */
    @Test public void testIsState() {
        Assert.assertTrue(nodeState.isState(NodeStateEnum.Starting));
        Assert.assertFalse(nodeState.isState(NodeStateEnum.AutoSync));
        Assert.assertTrue(nodeState.isState(NodeStateEnum.Starting, NodeStateEnum.Running));
        Assert.assertFalse(nodeState.isState(NodeStateEnum.AutoSync, NodeStateEnum.Running));
    }

    /**
     * Test not me node name reg.
     */
    @Test public void testNotMeNodeNameReg() {
        Assert.assertEquals(nodeState.notMeNodeNameReg(),
            "(?!" + nodeName.toUpperCase() + ")" + prefix.toUpperCase() + "(\\S)*");

    }
}