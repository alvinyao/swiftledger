package com.higgschain.trust.slave.core.api.impl;

import com.higgschain.trust.common.dao.RocksUtils;
import com.higgschain.trust.slave.api.RocksDbService;
import com.higgschain.trust.common.vo.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Rocks db service.
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class RocksDbServiceImpl implements RocksDbService {
    @Override public RespData getData(String columnFamily, String key) {
        RespData respData = new RespData();
        respData.setData(RocksUtils.getData(columnFamily, key));
        return respData;
    }
}
