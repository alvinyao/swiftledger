package com.higgschain.trust.consensus.p2pvalid.core;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Valid sync commit.
 *
 * @param <T> the type parameter
 * @author cwy
 */
@Slf4j public class ValidSyncCommit<T extends ValidCommand<?>> extends ValidBaseCommit<T> {

    /**
     * Instantiates a new Valid sync commit.
     *
     * @param validCommand the valid command
     */
    public ValidSyncCommit(T validCommand) {
        super(validCommand);
    }
}
