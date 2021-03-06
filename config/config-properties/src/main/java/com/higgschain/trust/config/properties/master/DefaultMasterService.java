package com.higgschain.trust.config.properties.master;

import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.config.listener.StateChangeListener;
import com.higgschain.trust.consensus.config.listener.StateListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Default master service.
 *
 * @author lingchao
 * @create 2018年07月12日17 :19
 */
@Service
@StateListener
public class DefaultMasterService {

    @Autowired
    private MasterConfig config;

    @Autowired
    private NodeState nodeState;

    /**
     * Sets master.
     */
    @StateChangeListener(value = NodeStateEnum.Running, before = true)
    public void setMaster() {
        String masterName = config.getMasterName();
        if (StringUtils.isBlank(masterName)) {
            masterName = nodeState.getNodeName();
        }
        nodeState.changeMaster(masterName);
    }
}
