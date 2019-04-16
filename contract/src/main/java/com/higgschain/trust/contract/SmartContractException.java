package com.higgschain.trust.contract;

/**
 * The type Smart contract exception.
 */
public class SmartContractException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Smart contract exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SmartContractException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Smart contract exception.
     *
     * @param message the message
     */
    public SmartContractException(String message) {
        super(message);
    }
}
