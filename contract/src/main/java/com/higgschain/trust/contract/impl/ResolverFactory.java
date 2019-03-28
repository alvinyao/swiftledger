package com.higgschain.trust.contract.impl;

import com.higgschain.trust.contract.ExecuteContext;

public interface ResolverFactory {
    Resolver createResolver(ExecuteContext context);
}
