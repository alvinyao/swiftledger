/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.management.failover.filter;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.core.command.AbstractConsensusCommand;
import com.higgschain.trust.consensus.core.filter.CommandFilter;
import com.higgschain.trust.consensus.core.filter.CommandFilterChain;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.model.bo.consensus.PackageCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The type Failover package filter.
 *
 * @author suimi
 * @date 2018 /6/21
 */
@Order(4) @Slf4j @Component public class FailoverPackageFilter implements CommandFilter {

    @Autowired private BlockRepository blockRepository;

    private AtomicLong blockHeight = new AtomicLong(0);

    @Override
    public void doFilter(ConsensusCommit<? extends AbstractConsensusCommand> commit, CommandFilterChain chain) {
        if (commit.operation() instanceof PackageCommand) {
            PackageCommand command = (PackageCommand)commit.operation();
            long packageHeight = command.getPackageHeight();
            if (packageHeight <= blockHeight.get()) {
                log.warn("package command:{} rejected,current block height:{}", packageHeight, blockHeight.get());
                commit.close();
                return;
            } else {
                blockHeight.set(blockRepository.getMaxHeight());
                if (packageHeight <= blockHeight.get()) {
                    log.warn("package command:{} rejected,current block height:{}", packageHeight, blockHeight.get());
                    commit.close();
                    return;
                }
            }
        }
        chain.doFilter(commit);
    }
}
