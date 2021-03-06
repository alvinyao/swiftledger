package com.higgschain.trust.slave.common;

import com.higgschain.trust.common.utils.IdGenerator;
import com.higgschain.trust.common.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by young001 on 2017/6/15.
 */
@Component
public class ReqIdGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReqIdGenerator.class);

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    /**
     * Generate req id string.
     *
     * @return the string
     */
    public final String generateReqId() {
        String requestId = null;
        try {
            String ipIntStr = String.valueOf(IpUtil.getHostIpInt());
            //        为了减少配置，我们默认使用一样的snowflake参数，但是加上ip生成唯一的requestid
            String id = String.valueOf(snowflakeIdWorker.nextId());
            requestId = new StringBuffer("reqid-").append(ipIntStr).append("-").append(id).toString();
        } catch (Exception e) {
            LOGGER.error("获取机器ip生成requestid失败，使用PPIdGenerator来生成reqid", e);
            requestId = IdGenerator.generateRandomReqId();
        }
        return requestId;
    }

    /**
     * Reqid parse string.
     *
     * @param reqId the req id
     * @return the string
     */
    public final String reqidParse(String reqId) {

        String[] reqIdList = reqId.split("-");

        if (reqIdList.length != 3) {
            return reqId + " can not parsed";
        }

        String ip = null;
        String date = null;
        try {
//            if parse to int fail,reqid generated by PPidGenerator
            int ipInt = Integer.parseInt(reqIdList[1]);
            ip = IpUtil.int2Ip(ipInt);
            Long snowflakeId = Long.parseLong(reqIdList[2]);
            SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
            date = idWorker.getTimeFromId(snowflakeId);
        } catch (Exception e) {
            ip = "127.0.0.1";
            Timestamp ts = new Timestamp(Long.parseLong(reqIdList[1]));
            date = ts.toString();
        }

        return new StringBuffer("host:").append(ip).append(",time:").append(date).toString();
    }

}
