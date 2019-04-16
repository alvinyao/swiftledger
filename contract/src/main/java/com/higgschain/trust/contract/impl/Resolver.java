package com.higgschain.trust.contract.impl;

/**
 * The interface Resolver.
 */
public interface Resolver {

    /**
     * Contains key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    boolean containsKey(Object key);

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    Object get(Object key);
}
