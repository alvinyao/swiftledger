/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.p2pvalid.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * The type Id valid command.
 *
 * @param <T> the type parameter
 * @author suimi
 * @date 2018 /4/24
 */
@Getter @Setter @NoArgsConstructor public abstract class IdValidCommand<T extends Serializable>
    extends ResponseCommand<T> {

    private static final long serialVersionUID = -5384005328220736154L;

    private String requestId;

    /**
     * Instantiates a new Id valid command.
     *
     * @param requestId the request id
     * @param t         the t
     */
    public IdValidCommand(String requestId, T t) {
        super(t);
        this.requestId = requestId;
    }

}
