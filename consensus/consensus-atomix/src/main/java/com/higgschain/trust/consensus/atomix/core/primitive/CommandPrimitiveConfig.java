/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.atomix.core.primitive;

import io.atomix.primitive.config.PrimitiveConfig;

/**
 * The type Command primitive config.
 *
 * @author suimi
 * @date 2018 /8/6
 */
public class CommandPrimitiveConfig extends PrimitiveConfig<CommandPrimitiveConfig> {

    private CommandPrimitiveType primitiveType;

    /**
     * Instantiates a new Command primitive config.
     *
     * @param primitiveType the primitive type
     */
    public CommandPrimitiveConfig(CommandPrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    @Override public CommandPrimitiveType getType() {
        return primitiveType;
    }
}
