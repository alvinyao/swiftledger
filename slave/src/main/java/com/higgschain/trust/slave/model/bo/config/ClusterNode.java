package com.higgschain.trust.slave.model.bo.config;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Cluster node.
 *
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 13:05
 */
@Getter @Setter public class ClusterNode extends BaseBO {

    private String nodeName;
    private boolean p2pStatus;
    private boolean rsStatus;
}
