/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.node;

import com.higgschain.trust.config.master.INodeInfoService;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.core.repository.PackageRepository;
import com.higgschain.trust.slave.model.enums.biz.PackageStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author suimi
 * @date 2018/6/12
 */
@Service public class NodeInfoService implements INodeInfoService {
    @Autowired private NodeState nodeState;

    @Autowired private BlockRepository blockRepository;

    @Autowired private PackageRepository packageRepository;

    @Value("${higgs.trust.electionMaster:false}") private boolean electionMaster;

    @Override public Long packageHeight() {
        return packageRepository.getMaxHeight();
    }

    @Override public Long blockHeight() {
        return blockRepository.getMaxHeight();
    }

    @Override public boolean hasMasterQualify() {
        if (!nodeState.isState(NodeStateEnum.Running) || !isElectionMaster()) {
            return false;
        }
        Long blockHeight = blockRepository.getMaxHeight();
        Long packageHeight = packageRepository.getMaxHeight();
        packageHeight = packageHeight == null ? 0 : packageHeight;
        if (blockHeight >= packageHeight) {
            return true;
        }
        List<PackageStatusEnum> packageStatusEnums = Arrays.asList(PackageStatusEnum.values());
        HashSet statusSet = new HashSet<>(packageStatusEnums);
        long count = packageRepository.count(statusSet, blockHeight);
        if (count >= packageHeight - blockHeight) {
            return true;
        }
        return false;
    }

    public boolean isElectionMaster() {
        return electionMaster;
    }

    public void setElectionMaster(boolean electionMaster) {
        this.electionMaster = electionMaster;
    }
}
