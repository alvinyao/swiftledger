package com.higgschain.trust.management.failover.service;

import com.higgschain.trust.consensus.config.NodeProperties;
import com.higgschain.trust.consensus.config.NodeStatefulService;
import com.higgschain.trust.network.NetworkManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * standby service
 *
 * @author lingchao
 * @create 2018年10月08日19 :53
 */
@Service
public class StandbyService extends NodeStatefulService {
    @Autowired
    private NodeProperties nodeProperties;


    @Override
    public String getStatefulServiceName() {
        return "NotStandby";
    }

    @Override
    protected void doStart() {
        nodeProperties.setStandby(false);

    }

    @Override
    protected void doPause() {
        nodeProperties.setStandby(true);
    }

    @Override
    protected void doResume() {
        nodeProperties.setStandby(false);
    }

    /**
     * restart net work
     */
    public void restartNetwork(){
        NetworkManage.getInstance().shutdown();
        NetworkManage.getInstance().config().setBackupNode(nodeProperties.isStandby());
        NetworkManage.getInstance().start();
    }

}
