package com.higgschain.trust.network;

/**
 * The interface Network listener.
 *
 * @author duhongming
 * @date 2018 /8/30
 */
public interface NetworkListener {
    /**
     * The enum Event.
     */
    public static enum Event {/**
     * Started event.
     */
    STARTED,
        /**
         * Join event.
         */
        JOIN,
        /**
         * Leave event.
         */
        LEAVE,
        /**
         * Offline event.
         */
        OFFLINE,
    }

    /**
     * handle
     *
     * @param event   the event
     * @param message the message
     */
    void handle(Event event, Object message);
}
