package com.higgschain.trust.slave.common.exception;

import com.higgschain.trust.common.exception.ErrorInfo;

/**
 * snapshot exception
 *
 * @author lingchao
 * @create 2018年04月17日17 :11
 */
public class SnapshotException extends SlaveException {

    /**
     * Instantiates a new Snapshot exception.
     *
     * @param e the e
     */
    public SnapshotException(SlaveException e) {
        super(e);
    }

    /**
     * Instantiates a new Snapshot exception.
     *
     * @param code the code
     */
    public SnapshotException(ErrorInfo code) {
        super(code);
    }

    /**
     * Instantiates a new Snapshot exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     */
    public SnapshotException(ErrorInfo code, String errorMessage) {
        super(code, errorMessage);
    }

    /**
     * Instantiates a new Snapshot exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public SnapshotException(ErrorInfo code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Snapshot exception.
     *
     * @param code         the code
     * @param errorMessage the error message
     * @param cause        the cause
     */
    public SnapshotException(ErrorInfo code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }


}