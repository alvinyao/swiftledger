/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.filter;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.core.command.AbstractConsensusCommand;

public interface CommandFilterChain {

    void doFilter(ConsensusCommit<? extends AbstractConsensusCommand> commit);
}
