/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.replicate;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;

/**
 * The type Default commit adapter.
 *
 * @param <T> the type parameter
 * @author suimi
 * @date 2018 /8/14
 */
public class DefaultCommitAdapter<T extends AbstractConsensusCommand> implements ConsensusCommit<T> {
    private T command;
    private boolean isClosed;

    /**
     * Instantiates a new Default commit adapter.
     *
     * @param object the object
     */
    public DefaultCommitAdapter(Object object) {
        if (object instanceof AbstractConsensusCommand) {
            this.command = (T)object;
        } else {
            throw new RuntimeException("the commit is not support!");
        }
    }

    @Override public T operation() {
        return command;
    }

    @Override public void close() {
        this.isClosed = true;
    }

    @Override public boolean isClosed() {
        return this.isClosed;
    }
}
