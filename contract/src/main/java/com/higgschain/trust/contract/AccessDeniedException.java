package com.higgschain.trust.contract;

/**
 * The type Access denied exception.
 *
 * @author duhongming
 * @date 2018 /6/11
 */
public class AccessDeniedException extends RuntimeException {

    /**
     * Instantiates a new Access denied exception.
     *
     * @param message the message
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}
