package com.higgschain.trust.contract;

/**
 * The interface Execute engine.
 *
 * @author duhongming
 * @date 2018 /04/25
 */
public interface ExecuteEngine {

    /**
     * The constant JAVASCRIPT.
     */
    public static final String JAVASCRIPT = "javascript";

    /**
     * execute smart contract by give method
     *
     * @param methodName the method name
     * @param bizArgs    the biz args
     * @return object
     */
    Object execute(String methodName, Object... bizArgs);
}
