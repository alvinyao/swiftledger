package com.higgschain.trust.network.message;

/**
 * The type Network message.
 *
 * @author duhongming
 * @date 2018 /9/3
 */
public abstract class NetworkMessage {
    /**
     * The enum Type.
     */
    public enum Type {/**
     * Request type.
     */
    REQUEST(1),
        /**
         * Response type.
         */
        RESPONSE(2);

        private final int id;

        Type(int id) {
            this.id = id;
        }

        /**
         * Returns the unique message type ID.
         *
         * @return the unique message type ID.
         */
        public int id() {
            return id;
        }

        /**
         * Returns the message type enum associated with the given ID.
         *
         * @param id the type ID.
         * @return the type enum for the given ID.
         */
        public static Type forId(int id) {
            switch (id) {
                case 1:
                    return REQUEST;
                case 2:
                    return RESPONSE;
                default:
                    throw new IllegalArgumentException("Unknown status ID " + id);
            }
        }
    }

    private long id;
    private byte[] payload;

    /**
     * Instantiates a new Network message.
     *
     * @param id      the id
     * @param payload the payload
     */
    public NetworkMessage(long id, byte[] payload) {
        this.id = id;
        this.payload = payload;
    }

    /**
     * Id long.
     *
     * @return the long
     */
    public long id() {
        return this.id;
    }

    /**
     * Payload byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] payload() {
        return payload;
    }

    /**
     * Returns the message type
     *
     * @return type
     */
    public abstract Type type();

    /**
     * Is request boolean.
     *
     * @return the boolean
     */
    public boolean isRequest() {
        return type() == Type.REQUEST;
    }
}
