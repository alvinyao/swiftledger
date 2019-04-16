package com.higgschain.trust.contract;

/**
 * The type Quota exceeded exception.
 *
 * @author duhongming
 * @date 2018 /6/7
 */
public class QuotaExceededException extends RuntimeException {
    /**
     * Instantiates a new Quota exceeded exception.
     *
     * @param message the message
     */
    public QuotaExceededException(String message) {
        super(message);
    }
}