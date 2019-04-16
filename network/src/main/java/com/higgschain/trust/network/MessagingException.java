package com.higgschain.trust.network;

import java.io.IOException;

/**
 * The type Messaging exception.
 *
 * @author duhongming
 * @date 2018 /8/23
 */
public class MessagingException extends IOException {

    /**
     * Instantiates a new Messaging exception.
     *
     * @param message the message
     */
    public MessagingException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Messaging exception.
     *
     * @param message the message
     * @param t       the t
     */
    public MessagingException(String message, Throwable t) {
        super(message, t);
    }

    /**
     * Instantiates a new Messaging exception.
     *
     * @param t the t
     */
    public MessagingException(Throwable t) {
        super(t);
    }

    /**
     * Exception indicating no remote registered remote handler.
     */
    public static class NoRemoteHandler extends MessagingException {
        /**
         * Instantiates a new No remote handler.
         */
        public NoRemoteHandler() {
            super("No remote message handler registered for this message");
        }
    }

    /**
     * Exception indicating handler failure.
     */
    public static class RemoteHandlerFailure extends MessagingException {
        /**
         * Instantiates a new Remote handler failure.
         */
        public RemoteHandlerFailure() {
            super("Remote handler failed to handle message");
        }
    }

    /**
     * Exception indicating failure due to invalid message structure such as an incorrect preamble.
     */
    public static class ProtocolException extends MessagingException {
        /**
         * Instantiates a new Protocol exception.
         */
        public ProtocolException() {
            super("Failed to process message due to invalid message structure");
        }
    }
}
