/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.filter;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.core.command.AbstractConsensusCommand;

/**
 * The interface Command filter.
 */
public interface CommandFilter {
    /**
     * Do filter.
     *
     * @param commit the commit
     * @param chain  the chain
     */
    void doFilter(ConsensusCommit<? extends AbstractConsensusCommand> commit, CommandFilterChain chain);
}
