package com.higgschain.trust.consensus.core;

/**
 * The interface Consensus commit.
 *
 * @param <T> the type parameter
 */
public interface ConsensusCommit<T> {
    /**
     * Operation t.
     *
     * @return the t
     */
    T operation();

    /**
     * Close.
     */
    void close();

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    boolean isClosed();
}
