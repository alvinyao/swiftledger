/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.replicate;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import com.higgschain.trust.consensus.filter.CompositeCommandFilter;

/**
 * The type Default commit replicate composite.
 *
 * @author suimi
 * @date 2018 /8/14
 */
public class DefaultCommitReplicateComposite extends AbstractCommitReplicateComposite {
    /**
     * Instantiates a new Default commit replicate composite.
     *
     * @param filter the filter
     */
    public DefaultCommitReplicateComposite(CompositeCommandFilter filter) {
        super(filter);
    }

    @Override public ConsensusCommit<? extends AbstractConsensusCommand> commitAdapter(Object request) {
        return new DefaultCommitAdapter<>(request);
    }
}
