package com.higgschain.trust.consensus.p2pvalid.core;

import lombok.ToString;

/**
 * The type P 2 p valid commit.
 *
 * @param <T> the type parameter
 * @author liuyu
 */
@ToString
public class P2PValidCommit<T extends ValidCommand<?>> extends ValidBaseCommit<T> {

    /**
     * The constant COMMAND_NORMAL.
     */
    public static final int COMMAND_NORMAL = 0;
    /**
     * The constant COMMAND_APPLIED.
     */
    public static final int COMMAND_APPLIED = 1;

    private int status;

    /**
     * Instantiates a new P 2 p valid commit.
     *
     * @param validCommand the valid command
     */
    public P2PValidCommit(ValidCommand<?> validCommand) {
        super((T)validCommand);
        this.status = COMMAND_NORMAL;
    }

    public void close() {
        this.status = COMMAND_APPLIED;
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        return this.status == COMMAND_APPLIED;
    }

}
