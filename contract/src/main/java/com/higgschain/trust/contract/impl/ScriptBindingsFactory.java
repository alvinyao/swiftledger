package com.higgschain.trust.contract.impl;

import com.higgschain.trust.contract.ExecuteContext;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Script bindings factory.
 */
public class ScriptBindingsFactory {
    /**
     * The Resolver factories.
     */
    protected List<ResolverFactory> resolverFactories;

    /**
     * Instantiates a new Script bindings factory.
     *
     * @param resolverFactories the resolver factories
     */
    public ScriptBindingsFactory(List<ResolverFactory> resolverFactories) {
        this.resolverFactories = resolverFactories;
    }

    /**
     * Create bindings bindings.
     *
     * @param context the context
     * @return the bindings
     */
    public Bindings createBindings(ExecuteContext context) {
        return new ScriptBindings(createResolvers(context), context);
    }

    /**
     * Create resolvers list.
     *
     * @param context the context
     * @return the list
     */
    protected List<Resolver> createResolvers(ExecuteContext context) {
        List<Resolver> scriptResolvers = new ArrayList<Resolver>();
        for (ResolverFactory scriptResolverFactory : resolverFactories) {
            Resolver resolver = scriptResolverFactory.createResolver(context);
            if (resolver != null) {
                scriptResolvers.add(resolver);
            }
        }
        return scriptResolvers;
    }

    /**
     * Gets resolver factories.
     *
     * @return the resolver factories
     */
    public List<ResolverFactory> getResolverFactories() {
        return resolverFactories;
    }

    /**
     * Sets resolver factories.
     *
     * @param resolverFactories the resolver factories
     */
    public void setResolverFactories(List<ResolverFactory> resolverFactories) {
        this.resolverFactories = resolverFactories;
    }
}
