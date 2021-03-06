package com.higgschain.trust.slave.dao.po.config;

import com.higgschain.trust.common.mybatis.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Cluster config po.
 *
 * @author WangQuanzhou
 * @desc cluster configuration
 * @date 2018 /6/5 10:27
 */
@Getter @Setter public class ClusterConfigPO extends BaseEntity {
    private String clusterName;

    private int nodeNum;

    private int faultNum;

    private Date createTime;

    private Date updateTime;
}
