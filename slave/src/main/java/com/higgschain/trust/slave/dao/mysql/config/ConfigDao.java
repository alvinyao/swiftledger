package com.higgschain.trust.slave.dao.mysql.config;

import com.higgschain.trust.slave.dao.po.config.ConfigPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Config dao.
 *
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 14:16
 */
@Mapper public interface ConfigDao {

    /**
     * Insert config.
     *
     * @param configPO the config po
     * @return
     * @desc insert config into db
     */
    void insertConfig(ConfigPO configPO);

    /**
     * Update config.
     *
     * @param configPO the config po
     * @return
     * @desc update config information
     */
    void updateConfig(ConfigPO configPO);

    /**
     * Gets config.
     *
     * @param configPO the config po
     * @return List config
     * @desc get config information by nodeName and usage(if needed)
     */
    List<ConfigPO> getConfig(ConfigPO configPO);

    /**
     * batch insert
     *
     * @param configPOList the config po list
     * @return int
     */
    int batchInsert(List<ConfigPO> configPOList);

    /**
     * batch update
     *
     * @param configPOList the config po list
     * @return int
     */
    int batchUpdate(List<ConfigPO> configPOList);
}
