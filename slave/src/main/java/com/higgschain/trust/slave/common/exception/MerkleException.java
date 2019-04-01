package com.higgschain.trust.slave.common.exception;

import com.higgschain.trust.common.exception.ErrorInfo;

/**
 * @author WangQuanzhou
 * @desc MerkleException
 * @date 2018/4/18 11:43
 */
public class MerkleException extends SlaveException {
    public MerkleException(SlaveException e) {
        super(e);
    }

    public MerkleException(ErrorInfo code) {
        super(code);
    }

    public MerkleException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    public MerkleException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    public MerkleException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}