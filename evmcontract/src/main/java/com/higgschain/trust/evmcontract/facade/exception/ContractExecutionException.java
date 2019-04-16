package com.higgschain.trust.evmcontract.facade.exception;

/**
 * Thrown to indicate that an exception happens during the contract is
 * executed in the virtual machine.
 *
 * @author Chen Jiawei
 * @date 2018 -11-16
 */
public class ContractExecutionException extends RuntimeException {
    private static final long serialVersionUID = 6661955926284899781L;

    /**
     * Instantiates a new Contract execution exception.
     *
     * @param message the message
     */
    public ContractExecutionException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Contract execution exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ContractExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
