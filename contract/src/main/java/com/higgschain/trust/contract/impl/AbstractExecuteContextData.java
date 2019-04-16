package com.higgschain.trust.contract.impl;

import com.higgschain.trust.contract.ExecuteContextData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Abstract execute context data.
 */
public abstract class AbstractExecuteContextData implements ExecuteContextData {

    private HashMap<String, Object> data;

    /**
     * Instantiates a new Abstract execute context data.
     */
    public AbstractExecuteContextData() {
        data = new HashMap<>();
    }

    /**
     * Instantiates a new Abstract execute context data.
     *
     * @param data the data
     */
    public AbstractExecuteContextData(Map<String, Object> data) {
        this();
        if (null != data) {
            data.forEach((key, value) -> {
                this.put(key, value);
            });
        }
    }

    @Override public Object get(String key) {
        return data.get(key);
    }

    @Override public ExecuteContextData put(String key, Object object) {
        data.put(key, object);
        return this;
    }

    @Override public Set<String> keySet() {
        return data.keySet();
    }
}
