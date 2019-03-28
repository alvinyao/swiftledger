/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.filter;

import com.higgschain.trust.common.enums.MonitorTargetEnum;
import com.higgschain.trust.common.utils.MonitorLogUtils;
import com.higgschain.trust.config.master.ChangeMasterService;
import com.higgschain.trust.config.master.MasterHeartbeatService;
import com.higgschain.trust.config.node.command.TermCommand;
import com.higgschain.trust.config.snapshot.TermManager;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.core.command.AbstractConsensusCommand;
import com.higgschain.trust.consensus.core.filter.CommandFilter;
import com.higgschain.trust.consensus.core.filter.CommandFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//import org.apache.commons.lang.ArrayUtils;

/**
 * @author suimi
 * @date 2018/6/1
 */
@Order(3) @Component @Slf4j public class TermFilter implements CommandFilter {
    @Autowired private NodeState nodeState;

    @Autowired private ChangeMasterService changeMasterService;

    @Autowired private MasterHeartbeatService masterHeartbeatService;

    @Autowired private TermManager termManager;

    @Override
    public void doFilter(ConsensusCommit<? extends AbstractConsensusCommand> commit, CommandFilterChain chain) {
        if (commit.operation() instanceof TermCommand) {
            TermCommand command = (TermCommand)commit.operation();
            Long term = command.getTerm();
            long height = command.getPackageHeight();

            String nodeName = command.getNodeName();
            if (!termManager.isTermHeight(term, nodeName, height)) {
                log.warn("package command rejected,current termInfo:{}", termManager.getTermInfo(term));
                MonitorLogUtils.logIntMonitorInfo(MonitorTargetEnum.REJECTED_PACKAGE_COMMAND, 1);
                commit.close();
                return;
            }
            if (term == nodeState.getCurrentTerm()) {
                termManager.resetEndHeight(height);
                changeMasterService.renewHeartbeatTimeout();
                if (nodeState.isMaster()) {
                    masterHeartbeatService.resetMasterHeartbeat();
                }
            }
        }
        chain.doFilter(commit);
    }
}
