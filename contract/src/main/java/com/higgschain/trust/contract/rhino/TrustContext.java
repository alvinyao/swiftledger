package com.higgschain.trust.contract.rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

/**
 * The type Trust context.
 *
 * @author duhongming
 * @date 2018 /6/7
 */
public class TrustContext extends Context {

    /**
     * The Quota.
     */
    protected int quota;

    /**
     * Instantiates a new Trust context.
     *
     * @param factory the factory
     */
    public TrustContext(ContextFactory factory) {
        super(factory);
    }
}
