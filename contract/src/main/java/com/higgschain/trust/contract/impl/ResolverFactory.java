package com.higgschain.trust.contract.impl;

import com.higgschain.trust.contract.ExecuteContext;

/**
 * The interface Resolver factory.
 */
public interface ResolverFactory {
    /**
     * Create resolver resolver.
     *
     * @param context the context
     * @return the resolver
     */
    Resolver createResolver(ExecuteContext context);
}
