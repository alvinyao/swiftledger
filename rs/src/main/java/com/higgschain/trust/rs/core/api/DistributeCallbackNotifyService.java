package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.core.api.enums.RedisMegGroupEnum;
import com.higgschain.trust.common.vo.RespData;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * distribute callback notify
 *
 * @author lingchao
 * @create 2018年08月23日14 :27
 */
public interface DistributeCallbackNotifyService {
    /**
     * notify callback finish
     *
     * @param respDatas         the resp datas
     * @param redisMegGroupEnum the redis meg group enum
     */
    void notifySyncResult(List<RespData<String>> respDatas, RedisMegGroupEnum redisMegGroupEnum);

    /**
     * sync wait RespData
     *
     * @param key               the key
     * @param redisMegGroupEnum the redis meg group enum
     * @param timeout           the timeout
     * @param timeUnit          the time unit
     * @return resp data
     */
    RespData syncWaitNotify(String key, RedisMegGroupEnum redisMegGroupEnum, long timeout, TimeUnit timeUnit);
}
