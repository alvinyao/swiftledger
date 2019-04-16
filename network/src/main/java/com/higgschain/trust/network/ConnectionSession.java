package com.higgschain.trust.network;

import io.netty.util.AttributeKey;

/**
 * The type Connection session.
 *
 * @author duhongming
 * @date 2018 /9/6
 */
public final class ConnectionSession {

    /**
     * The constant ATTR_KEY_CONNECTION_SESSION.
     */
    public static final AttributeKey<ConnectionSession> ATTR_KEY_CONNECTION_SESSION = AttributeKey.newInstance("ConnectionSession");

    /**
     * The enum Channel type.
     */
    public static enum  ChannelType {/**
     * Outbound channel type.
     */
    OUTBOUND,
        /**
         * Inbound channel type.
         */
        INBOUND;
    }

    private Address remoteAddress;
    private ChannelType channelType;

    /**
     * Instantiates a new Connection session.
     *
     * @param remoteAddress the remote address
     * @param channelType   the channel type
     */
    public ConnectionSession(Address remoteAddress, ChannelType channelType) {
        this.remoteAddress = remoteAddress;
        this.channelType = channelType;
    }

    /**
     * Gets remote address.
     *
     * @return the remote address
     */
    public Address getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Gets channel type.
     *
     * @return the channel type
     */
    public ChannelType getChannelType() {
        return channelType;
    }
}
