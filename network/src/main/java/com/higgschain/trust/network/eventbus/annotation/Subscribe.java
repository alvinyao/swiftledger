package com.higgschain.trust.network.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Subscribe.
 *
 * @author duhongming
 * @date 2018 /8/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    /**
     * Topic string.
     *
     * @return the string
     */
    String topic() default "default-topic";
}
