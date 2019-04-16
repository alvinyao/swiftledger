/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.management.exception;

import com.higgschain.trust.common.exception.ErrorInfo;
import com.higgschain.trust.slave.common.exception.SlaveException;

/**
 * The type Failover execption.
 *
 * @author suimi
 * @date 2018 /4/17
 */
public class FailoverExecption extends SlaveException {

    /**
     * Instantiates a new Failover execption.
     *
     * @param e the e
     */
    public FailoverExecption(SlaveException e) {
        super(e);
    }

    /**
     * Instantiates a new Failover execption.
     *
     * @param code the code
     */
    public FailoverExecption(ErrorInfo code) {
        super(code);
    }

    /**
     * Instantiates a new Failover execption.
     *
     * @param code         the code
     * @param errorMessage the error message
     */
    public FailoverExecption(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    /**
     * Instantiates a new Failover execption.
     *
     * @param code  the code
     * @param cause the cause
     */
    public FailoverExecption(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Failover execption.
     *
     * @param code         the code
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public FailoverExecption(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}
