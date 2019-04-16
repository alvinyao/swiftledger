package com.higgschain.trust.slave.core.repository;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.model.bo.manage.RegisterRS;
import com.higgschain.trust.slave.model.bo.manage.RsNode;
import com.higgschain.trust.slave.model.bo.manage.RsPubKey;
import com.higgschain.trust.slave.model.enums.biz.RsNodeStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * The type Rs node repository test.
 */
/*
 *
 * @desc
 * @author tangfashuang
 * @date 2018/4/14
 *
 */
public class RsNodeRepositoryTest extends BaseTest {
    @Autowired
    private RsNodeRepository rsNodeRepository;

    private RsNode rsNode;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @BeforeMethod public void setUp() throws Exception {
        rsNode = new RsNode();
        rsNode.setRsId("rs-test3");
        rsNode.setStatus(RsNodeStatusEnum.COMMON);
        rsNode.setDesc("rs-test3");
    }

    /**
     * Query all.
     */
    @Test public void queryAll() {
        List<RsNode> rsNodeList = rsNodeRepository.queryAll();
        rsNodeList.forEach(rsNode -> {
            System.out.println(rsNode);
        });
    }

    /**
     * Query by rs id return null.
     */
    // cannot acuqire rsNode
    @Test public void queryByRsIdReturnNull() {
        RsNode rsNode = rsNodeRepository.queryByRsId("test");
        assertEquals(null, rsNode);
    }

    /**
     * Query by rs id.
     */
    // success
    @Test public void queryByRsId() {
        RsNode rsNode = rsNodeRepository.queryByRsId("rs-test3");
        System.out.println(rsNode);
    }

    /**
     * Convert action to rs node.
     */
    @Test public void convertActionToRsNode() {
        RegisterRS registerRS = new RegisterRS();
        registerRS.setRsId("rs-test4");
        registerRS.setDesc("rs-test4-RsNode");

        RsNode rs = rsNodeRepository.convertActionToRsNode(registerRS);
        assertEquals(rs.getRsId(), registerRS.getRsId());

    }

    /**
     * Test query rs and pub key.
     *
     * @throws Exception the exception
     */
    @Test public void testQueryRsAndPubKey() throws Exception {
        List<RsPubKey> rsPubKeyList = rsNodeRepository.queryRsAndPubKey();
        System.out.println(rsPubKeyList);
    }
}