package com.higgschain.trust.consensus.bftsmartcustom.started.custom.adapter;

import com.higgschain.trust.consensus.core.ConsensusCommit;
import com.higgschain.trust.consensus.command.AbstractConsensusCommand;

/**
 * The type Smart commit adapter.
 *
 * @param <T> the type parameter
 */
public class SmartCommitAdapter<T extends AbstractConsensusCommand> implements ConsensusCommit<T> {

    private T command;
    private boolean isClosed;

    /**
     * Instantiates a new Smart commit adapter.
     *
     * @param object the object
     */
    public SmartCommitAdapter(Object object) {
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
