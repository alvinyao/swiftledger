package com.higgschain.trust.slave.common.exception;

import com.higgschain.trust.common.exception.ErrorInfo;

/**
 * The type Merkle exception.
 *
 * @author WangQuanzhou
 * @desc MerkleException
 * @date 2018 /4/18 11:43
 */
public class MerkleException extends SlaveException {
    /**
     * Instantiates a new Merkle exception.
     *
     * @param e the e
     */
    public MerkleException(SlaveException e) {
        super(e);
    }

    /**
     * Instantiates a new Merkle exception.
     *
     * @param code the code
     */
    public MerkleException(ErrorInfo code) {
        super(code);
    }

    /**
     * Instantiates a new Merkle exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     */
    public MerkleException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    /**
     * Instantiates a new Merkle exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public MerkleException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Merkle exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public MerkleException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}