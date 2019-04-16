package com.higgschain.trust.consensus.p2pvalid.core;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Valid base commit.
 *
 * @param <T> the type parameter
 * @author cwy
 */
@Slf4j public class ValidBaseCommit<T extends ValidCommand<?>> {

    private T validCommand;

    /**
     * Instantiates a new Valid base commit.
     *
     * @param validCommand the valid command
     */
    protected ValidBaseCommit(T validCommand) {
        this.validCommand = validCommand;
    }

    /**
     * Operation t.
     *
     * @return the t
     */
    public T operation() {
        return validCommand;
    }

    /**
     * Type class.
     *
     * @return the class
     */
    public Class<? extends ValidCommand> type() {
        return validCommand.getClass();
    }

    /**
     * Close.
     */
    public void close() {
    }

}
