/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.atomix.core.primitive;

import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import io.atomix.primitive.AsyncPrimitive;

import java.util.concurrent.CompletableFuture;

/**
 * The interface Async command primitive.
 *
 * @author suimi
 * @date 2018 /7/6
 */
public interface IAsyncCommandPrimitive extends AsyncPrimitive{

    /**
     * Submit completable future.
     *
     * @param command the command
     * @return the completable future
     */
    CompletableFuture<Void> submit(AbstractConsensusCommand command);

}
