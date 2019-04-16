/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.exception;

import com.higgschain.trust.common.exception.ErrorInfo;
import com.higgschain.trust.common.exception.TrustException;

/**
 * The type Consensus exception.
 *
 * @author suimi
 * @date 2018 /6/12
 */
public class ConsensusException extends TrustException {
    private static final long serialVersionUID = -3350277997782674259L;

    /**
     * Instantiates a new Consensus exception.
     *
     * @param e the e
     */
    public ConsensusException(TrustException e) {
        super(e);
    }

    /**
     * Instantiates a new Consensus exception.
     *
     * @param code the code
     */
    public ConsensusException(ErrorInfo code) {
        super(code);
    }

    /**
     * Instantiates a new Consensus exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     */
    public ConsensusException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    /**
     * Instantiates a new Consensus exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public ConsensusException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Consensus exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public ConsensusException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}
