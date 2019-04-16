package com.higgschain.trust.slave.common.util.asynctosync;

/**
 * The interface Blocking map.
 *
 * @param <V> the type parameter
 */
public interface BlockingMap<V> {
    /**
     * Put.
     *
     * @param key the key
     * @param o   the o
     * @throws InterruptedException the interrupted exception
     */
    void put(String key, V o) throws InterruptedException;

    /**
     * Take v.
     *
     * @param key the key
     * @return the v
     * @throws InterruptedException the interrupted exception
     */
    V take(String key) throws InterruptedException;

    /**
     * Poll v.
     *
     * @param key     the key
     * @param timeout the timeout
     * @return the v
     * @throws InterruptedException the interrupted exception
     */
    V poll(String key, long timeout) throws InterruptedException;
}
