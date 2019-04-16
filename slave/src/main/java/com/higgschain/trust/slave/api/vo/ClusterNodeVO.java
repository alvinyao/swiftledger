package com.higgschain.trust.slave.api.vo;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Cluster node vo.
 *
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 13:05
 */
@Getter @Setter public class ClusterNodeVO extends BaseBO {
    private String reqNo;

    private String nodeName;
    private String p2pStatus;
    private String rsStatus;
}
