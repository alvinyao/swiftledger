package com.higgs.trust.slave.common.exception;

import com.higgschain.trust.common.exception.ErrorInfo;
import com.higgschain.trust.common.exception.TrustException;

public class SlaveException extends TrustException {

    public SlaveException(TrustException e) {
        super(e);
    }

    public SlaveException(ErrorInfo code) {
        super(code);
    }

    public SlaveException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    public SlaveException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    public SlaveException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }
}