/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.common.exception;

/**
 * The interface Error info.
 *
 * @author suimi
 * @date 2018 /6/12
 */
public interface ErrorInfo {
    /**
     * Gets code.
     *
     * @return Returns the code.
     */
    String getCode();

    /**
     * Gets description.
     *
     * @return Returns the description.
     */
    String getDescription();

    /**
     * Is need retry boolean.
     *
     * @return boolean
     */
    boolean isNeedRetry();
}
