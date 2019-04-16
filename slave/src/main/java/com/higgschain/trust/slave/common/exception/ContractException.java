package com.higgschain.trust.slave.common.exception;

import com.higgschain.trust.common.exception.ErrorInfo;

/**
 * The type Contract exception.
 *
 * @author duhongming
 * @date 2018 /04/25
 */
public class ContractException extends SlaveException {
    /**
     * Instantiates a new Contract exception.
     *
     * @param e the e
     */
    public ContractException(SlaveException e) {
        super(e);
    }

    /**
     * Instantiates a new Contract exception.
     *
     * @param code the code
     */
    public ContractException(ErrorInfo code) {
        super(code);
    }

    /**
     * Instantiates a new Contract exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     */
    public ContractException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    /**
     * Instantiates a new Contract exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public ContractException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Contract exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public ContractException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}
