package com.higgschain.trust.slave.dao;

import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.dao.mysql.manage.RsNodeDao;
import com.higgschain.trust.slave.dao.po.manage.RsNodePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * The type Rs node dao test.
 *
 * @author tangfashuang
 * @date 2018 /04/13 22:19
 * @desc rs node dao test
 */
public class RsNodeDaoTest extends BaseTest {
    /**
     * The Rs node dao.
     */
    @Autowired RsNodeDao rsNodeDao;

    /**
     * Test add 1.
     */
    @Test public void testAdd1() {
        RsNodePO rsNodePO = new RsNodePO();
        rsNodePO.setRsId("rs-test1");
        rsNodePO.setDesc("rs-test1-desc");

        rsNodeDao.add(rsNodePO);
    }

    /**
     * Test add 2.
     */
    @Test public void testAdd2() {
        RsNodePO rsNodePO = new RsNodePO();
        rsNodePO.setRsId("rs-test2");
        rsNodePO.setDesc("rs-test2-desc");

        rsNodeDao.add(rsNodePO);
    }

    /**
     * Test add 3.
     */
    @Test public void testAdd3() {
        RsNodePO rsNodePO = new RsNodePO();
        rsNodePO.setRsId("rs-test3");
        rsNodePO.setDesc("rs-test3-desc");

        rsNodeDao.add(rsNodePO);
    }
}