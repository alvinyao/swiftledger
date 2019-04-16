/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.exception;

import com.higgschain.trust.common.exception.ErrorInfo;
import com.higgschain.trust.common.exception.TrustException;

/**
 * The type Config exception.
 *
 * @author suimi
 * @date 2018 /6/12
 */
public class ConfigException extends TrustException {
    private static final long serialVersionUID = -3350277997782674259L;

    /**
     * Instantiates a new Config exception.
     *
     * @param e the e
     */
    public ConfigException(TrustException e) {
        super(e);
    }

    /**
     * Instantiates a new Config exception.
     *
     * @param code the code
     */
    public ConfigException(ErrorInfo code) {
        super(code);
    }

    /**
     * Instantiates a new Config exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     */
    public ConfigException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    /**
     * Instantiates a new Config exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public ConfigException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Config exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public ConfigException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}
