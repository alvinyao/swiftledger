package com.higgschain.trust.network.eventbus;

import java.lang.reflect.Method;

/**
 * The type Subscriber.
 *
 * @author duhongming
 * @date 2018 /8/16
 */
public class Subscriber {
    private final Object subscribe;
    private final Method method;
    private boolean disable;

    /**
     * Instantiates a new Subscriber.
     *
     * @param subscribe the subscribe
     * @param method    the method
     */
    public Subscriber(Object subscribe, Method method) {
        this.subscribe = subscribe;
        this.method = method;
    }

    /**
     * Gets subscribe object.
     *
     * @return the subscribe object
     */
    public Object getSubscribeObject() {
        return subscribe;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Is disable boolean.
     *
     * @return the boolean
     */
    public boolean isDisable() {
        return disable;
    }

    /**
     * Sets disable.
     *
     * @param b the b
     */
    public void setDisable(boolean b) {
    }
}
