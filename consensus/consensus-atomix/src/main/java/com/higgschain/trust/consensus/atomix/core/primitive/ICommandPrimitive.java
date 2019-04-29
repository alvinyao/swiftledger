/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.atomix.core.primitive;

import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import io.atomix.primitive.SyncPrimitive;

/**
 * The interface Command primitive.
 */
public interface ICommandPrimitive extends SyncPrimitive {

    /**
     * Submit.
     *
     * @param command the command
     */
    void submit(AbstractConsensusCommand command);

    @Override IAsyncCommandPrimitive async();
}
