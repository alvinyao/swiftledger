package com.higgschain.trust.consensus.core.command;

import java.io.Serializable;

/**
 * The interface Consensus command.
 *
 * @param <T> the type parameter
 */
public interface ConsensusCommand<T> extends Serializable {
    /**
     * Get t.
     *
     * @return the t
     */
    T get();
}
