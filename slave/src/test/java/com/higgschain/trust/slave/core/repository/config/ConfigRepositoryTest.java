package com.higgschain.trust.slave.core.repository.config;

import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.common.dao.RocksUtils;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.api.enums.VersionEnum;
import com.higgschain.trust.slave.dao.po.config.ConfigPO;
import com.higgschain.trust.slave.dao.rocks.config.ConfigRocksDao;
import com.higgschain.trust.slave.model.bo.config.Config;
import com.higgschain.trust.slave.model.enums.UsageEnum;
import org.rocksdb.Transaction;
import org.rocksdb.WriteOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Config repository test.
 */
public class ConfigRepositoryTest extends BaseTest {

    @Autowired private ConfigRepository configRepository;
    @Autowired private ConfigRocksDao configRocksDao;

    /**
     * Test insert config.
     *
     * @throws Exception the exception
     */
    @Test public void testInsertConfig() throws Exception {
        Config config = new Config();
        config.setNodeName("node-1");
        config.setVersion(VersionEnum.V1.getCode());
        config.setPriKey("prikey");
        config.setPubKey("pubkey");
        config.setValid(true);
        config.setTmpPriKey("tempPriKey");
        config.setTmpPubKey("tempPubKey");
        config.setUsage(UsageEnum.BIZ.getCode());
        configRepository.insertConfig(config);

        Config c = configRepository.getBizConfig("node-1");
        System.out.println(c);
    }

    /**
     * Test update config.
     *
     * @throws Exception the exception
     */
    @Test public void testUpdateConfig() throws Exception {
        Config config = new Config();
        ConfigPO po = configRocksDao.get("node-1" + Constant.SPLIT_SLASH + UsageEnum.BIZ.getCode());
        BeanUtils.copyProperties(po, config);
        config.setValid(false);
        configRepository.updateConfig(config);

        Config c = configRepository.getBizConfig("node-1");
        Assert.assertEquals(c.isValid(), false);
    }

    /**
     * Test get config.
     *
     * @throws Exception the exception
     */
    @Test public void testGetConfig() throws Exception {
        System.out.println(configRepository.getBizConfig("node-1"));
    }

    /**
     * Test batch insert.
     *
     * @throws Exception the exception
     */
    @Test public void testBatchInsert() throws Exception {
        List list = new LinkedList();
        for (int i = 0; i < 5; i++) {
            ConfigPO config = new ConfigPO();
            config.setNodeName("node-" + i);
            config.setVersion(VersionEnum.V1.getCode());
            config.setPriKey("prikey");
            config.setPubKey("pubkey");
            config.setValid(true);
            config.setTmpPriKey("tempPriKey");
            config.setTmpPubKey("tempPubKey");
            config.setUsage(UsageEnum.CONSENSUS.getCode());
            list.add(config);
        }
        configRepository.batchInsert(list);
    }

    /**
     * Test batch update.
     *
     * @throws Exception the exception
     */
    @Test public void testBatchUpdate() throws Exception {
        List list = new LinkedList();
        for (int i = 1; i < 2; i++) {
            ConfigPO config = new ConfigPO();
            config.setNodeName("node-" + i);
            config.setPriKey("prikey"+i);
            config.setPubKey("pubkey"+i);
            config.setValid(true);
            config.setTmpPriKey("haha"+i);
            config.setTmpPubKey("hehe"+i);
            config.setUsage(UsageEnum.BIZ.getCode());
            list.add(config);
        }
        Transaction tx = RocksUtils.beginTransaction(new WriteOptions());
        ThreadLocalUtils.putRocksTx(tx);
        configRepository.batchUpdate(list);
        RocksUtils.txCommit(tx);
        ThreadLocalUtils.clearRocksTx();
        Config c = configRepository.getBizConfig("node-1");
        System.out.println(c);
    }
}