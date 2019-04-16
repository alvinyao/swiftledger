package com.higgschain.trust.slave.dao.mysql.config;

import com.higgschain.trust.common.mybatis.BaseDao;
import com.higgschain.trust.slave.dao.po.config.SystemPropertyPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * System  Property  dao
 *
 * @author lingchao
 * @create 2018年06月27日15 :58
 */
@Mapper
public interface SystemPropertyDao extends BaseDao<SystemPropertyPO> {

    /**
     * query  system property by key
     *
     * @param key the key
     * @return system property po
     */
    SystemPropertyPO queryByKey(@Param("key") String key);

    /**
     * query  system property by key
     *
     * @param key   the key
     * @param value the value
     * @param desc  the desc
     * @return int
     */
    int update(@Param("key") String key, @Param("value") String value, @Param("desc") String desc);

}
