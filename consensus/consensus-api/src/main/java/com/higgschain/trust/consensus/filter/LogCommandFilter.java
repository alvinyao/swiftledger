/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.filter;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The type Log command filter.
 *
 * @author suimi
 * @date 2018 /6/1
 */
@Order(0)
@Component @Slf4j public class LogCommandFilter implements CommandFilter {
    @Override
    public void doFilter(ConsensusCommit<? extends AbstractConsensusCommand> commit, CommandFilterChain chain) {
        AbstractConsensusCommand operation = commit.operation();
        if (log.isDebugEnabled()) {
            log.debug("received command:{}", operation);
        }
        chain.doFilter(commit);
    }
}
