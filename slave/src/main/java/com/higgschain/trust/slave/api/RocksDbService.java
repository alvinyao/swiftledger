package com.higgschain.trust.slave.api;

import com.higgschain.trust.slave.api.vo.RespData;

/**
 * @author tangfashuang
 */
public interface RocksDbService {
    /**
     * get data
     * @param columnFamily
     * @param key
     * @return
     */
    RespData getData(String columnFamily, String key);
}
