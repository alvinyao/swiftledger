package com.higgschain.trust.evmcontract.facade.exception;

/**
 * Thrown to indicate that the contract context contains an illegal
 * or inappropriate status given by contract builder.
 *
 * @author Chen Jiawei
 * @date 2018 -11-15
 */
public class ContractContextException extends IllegalArgumentException {
    private static final long serialVersionUID = 7373404340319963398L;

    /**
     * Instantiates a new Contract context exception.
     *
     * @param message the message
     */
    public ContractContextException(String message) {
        super(message);
    }
}
