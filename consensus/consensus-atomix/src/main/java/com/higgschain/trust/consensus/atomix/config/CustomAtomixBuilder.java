/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.atomix.config;

import io.atomix.core.AtomixBuilder;
import io.atomix.core.AtomixConfig;
import io.atomix.core.AtomixRegistry;

/**
 * The type Custom atomix builder.
 *
 * @author suimi
 * @date 2018 /8/8
 */
public class CustomAtomixBuilder extends AtomixBuilder {
    /**
     * Instantiates a new Custom atomix builder.
     *
     * @param config   the config
     * @param registry the registry
     */
    protected CustomAtomixBuilder(AtomixConfig config, AtomixRegistry registry) {
        super(config, registry);
    }
}
