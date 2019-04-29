/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.atomix.core.primitive;

import com.higgschain.trust.consensus.command.AbstractConsensusCommand;
import io.atomix.primitive.operation.Command;

/**
 * The interface Command primitive service.
 *
 * @author suimi
 * @date 2018 /7/6
 */
public interface ICommandPrimitiveService {

    /**
     * Submit.
     *
     * @param command the command
     */
    @Command void submit(AbstractConsensusCommand command);
}
