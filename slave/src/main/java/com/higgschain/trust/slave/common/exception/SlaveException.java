package com.higgschain.trust.slave.common.exception;

import com.higgschain.trust.common.exception.ErrorInfo;
import com.higgschain.trust.common.exception.TrustException;

/**
 * The type Slave exception.
 */
public class SlaveException extends TrustException {

    /**
     * Instantiates a new Slave exception.
     *
     * @param e the e
     */
    public SlaveException(TrustException e) {
        super(e);
    }

    /**
     * Instantiates a new Slave exception.
     *
     * @param code the code
     */
    public SlaveException(ErrorInfo code) {
        super(code);
    }

    /**
     * Instantiates a new Slave exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     */
    public SlaveException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    /**
     * Instantiates a new Slave exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public SlaveException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Slave exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public SlaveException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}