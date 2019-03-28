package com.higgschain.trust.contract.impl;

public interface Resolver {

    boolean containsKey(Object key);

    Object get(Object key);
}
