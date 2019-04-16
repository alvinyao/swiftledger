package com.higgschain.trust.consensus.config.listener;

import com.higgschain.trust.consensus.config.NodeStateEnum;

import java.lang.annotation.*;

/**
 * The interface State change listener.
 */
@Documented @Target({ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME) public @interface StateChangeListener {

    /**
     * Value node state enum [ ].
     *
     * @return the node state enum [ ]
     */
    NodeStateEnum[] value();

    /**
     * Before boolean.
     *
     * @return the boolean
     */
    boolean before() default false;
}
