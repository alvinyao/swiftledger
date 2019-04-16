package com.higgschain.trust.contract;

import java.util.Set;

/**
 * smart contract runtime context data
 *
 * @author duhongming
 * @date 2018 -04-17
 */
public interface ExecuteContextData {

    /**
     * The constant KEY_CONTRACT_INSTANCE_ID.
     */
    public static final String KEY_CONTRACT_INSTANCE_ID = "CONTRACT_INSTANCE_ID";
    /**
     * The constant KEY_CONTRACT_ADDRESS.
     */
    public static final String KEY_CONTRACT_ADDRESS = "CONTRACT_ADDRESS";

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    Object get(String key);

    /**
     * Put execute context data.
     *
     * @param key    the key
     * @param object the object
     * @return the execute context data
     */
    ExecuteContextData put(String key, Object object);

    /**
     * Key set set.
     *
     * @return the set
     */
    Set<String> keySet();
}
