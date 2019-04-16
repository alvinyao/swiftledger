package com.higgschain.trust.network.message.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Message handler.
 *
 * @author duhongming
 * @date 2018 /9/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageHandler {
    /**
     * Name string.
     *
     * @return the string
     */
    String name();
}
