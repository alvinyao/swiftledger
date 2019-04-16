package com.higgschain.trust.slave.api;

import com.higgschain.trust.common.vo.RespData;

/**
 * The interface Rocks db service.
 *
 * @author tangfashuang
 */
public interface RocksDbService {
    /**
     * get data
     *
     * @param columnFamily the column family
     * @param key          the key
     * @return data
     */
    RespData getData(String columnFamily, String key);
}
