package com.higgschain.trust.common.dao;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Rocks db helper.
 *
 * @author liuyu
 * @description
 * @date 2018 -09-13
 */
@Component public class RocksDBHelper extends RocksBaseDao<Object> {
    /**
     * the definition of table name
     */
    private String tableName;

    @Override protected String getColumnFamilyName() {
        return tableName;
    }

    /**
     * query
     *
     * @param tableName the table name
     * @param key       the key
     * @return object
     */
    public Object queryByKey(String tableName, String key) {
        setTableName(tableName);
        return get(key);
    }

    /**
     * query by prefix
     *
     * @param tableName the table name
     * @param prefix    the prefix
     * @param limit     the limit
     * @return list
     */
    public List<Object> queryByPrefix(String tableName, String prefix, int limit) {
        setTableName(tableName);
        return queryByPrefix(prefix, limit == 0 ? -1 : limit);
    }

    /**
     * query by count and order
     *
     * @param tableName the table name
     * @param count     the count
     * @param order     the order
     * @return list
     */
    public List<Object> queryByCount(String tableName, int count, int order) {
        setTableName(tableName);
        if (count > 1000) {
            count = 1000;
        }
        return queryByPrefix(null, count, order);
    }

    /**
     * count by
     *
     * @param tableName the table name
     * @param prefix    the prefix
     * @return long
     */
    public long countBy(String tableName, String prefix) {
        setTableName(tableName);
        return count(prefix);
    }

    /**
     * clear tables
     *
     * @param tableNames the table names
     * @return boolean
     */
    public boolean clear(String[] tableNames) {
        if (ArrayUtils.isEmpty(tableNames)) {
            return false;
        }
        for (String tableName : tableNames) {
            setTableName(tableName);
            String beginKey = queryFirstKey(null);
            String endKey = queryLastKey();
            deleteRange(beginKey, endKey);
            delete(endKey);
        }
        return true;
    }

    /**
     * clear all tables
     *
     * @param ignoreTables the ignore tables
     * @return boolean
     */
    public boolean clearAll(String[] ignoreTables) {
        List<String> tableNames = showTables();
        if (CollectionUtils.isEmpty(tableNames)) {
            return false;
        }
        Map<String, String> ignoredMap = new HashMap<>();
        if (!ArrayUtils.isEmpty(ignoreTables)) {
            for (String ignored : ignoreTables) {
                ignoredMap.put(ignored, ignored);
            }
        }
        List<String> list = new ArrayList<>();
        for (String name : tableNames) {
            if (!ignoredMap.containsKey(name)) {
                list.add(name);
            }
        }
        clear(list.toArray(new String[] {}));
        return true;
    }

    /**
     * set table name
     *
     * @param tableName
     */
    private void setTableName(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new RuntimeException("table name is null");
        }
        this.tableName = tableName.trim();
    }

    /**
     * put value
     *
     * @param tableName the table name
     * @param key       the key
     * @param value     the value
     */
    public void put(String tableName, String key, Object value) {
        setTableName(tableName);
        put(key, value);
    }
}
