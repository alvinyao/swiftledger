package com.higgschain.trust.slave.core.service.snapshot;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * loading cache service
 *
 * @author lingchao
 * @create 2018年04月10日0 :59
 */
public interface CacheLoader {
    /**
     * the method to query object from db
     *
     * @param object the object
     * @return object
     */
    Object query(Object object);

    /**
     * the method to batchInsert data into db
     *
     * @param insertList the insert list
     * @return boolean
     */
    boolean batchInsert(List<Pair<Object, Object>> insertList);

    /**
     * the method to batchUpdate data into db
     *
     * @param updateList the update list
     * @return boolean
     */
    boolean batchUpdate(List<Pair<Object, Object>> updateList);

}
