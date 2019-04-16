package com.higgschain.trust.contract.impl;

import com.higgschain.trust.contract.ExecuteContext;

import java.util.Map;

/**
 * The type Services resolver factory.
 *
 * @author duhongming
 * @date 2018 /04/25
 */
public class ServicesResolverFactory implements ResolverFactory, Resolver {

    private Map<String, Object> services;

    /**
     * Instantiates a new Services resolver factory.
     *
     * @param variables the variables
     */
    public ServicesResolverFactory(Map<String, Object> variables) {
        this.services = variables;
    }

    @Override public boolean containsKey(Object key) {
        return services.containsKey(key);
    }

    @Override public Object get(Object key) {
        return services.get(key);
    }

    @Override public Resolver createResolver(ExecuteContext context) {
        return this;
    }
}
