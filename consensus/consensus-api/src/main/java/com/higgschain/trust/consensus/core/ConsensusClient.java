package com.higgschain.trust.consensus.core;

import com.higgschain.trust.consensus.core.command.AbstractConsensusCommand;

import java.util.concurrent.CompletableFuture;

/**
 * The interface Consensus client.
 *
 * @author cwy
 */
public interface ConsensusClient {
    /**
     * Submit completable future.
     *
     * @param <T>     generic type of load
     * @param command command context
     * @return completable future
     */
    <T> CompletableFuture<?> submit(AbstractConsensusCommand<T> command);

}
