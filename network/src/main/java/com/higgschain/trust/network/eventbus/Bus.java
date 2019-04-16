package com.higgschain.trust.network.eventbus;

/**
 * The interface Bus.
 *
 * @author duhongming
 * @date 2018 /8/16
 */
public interface Bus {

    /**
     * Register.
     *
     * @param subscriber the subscriber
     */
    void register(Object subscriber);

    /**
     * Unregister.
     *
     * @param subscriber the subscriber
     */
    void unregister(Object subscriber);

    /**
     * Post.
     *
     * @param event the event
     */
    void post(Object event);

    /**
     * Post.
     *
     * @param event the event
     * @param topic the topic
     */
    void post(Object event, String topic);

    /**
     * Close.
     */
    void close();

    /**
     * Gets bus name.
     *
     * @return the bus name
     */
    String getBusName();
}
