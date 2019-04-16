package com.higgschain.trust.network.message;

import com.higgschain.trust.network.Address;

/**
 * The type Network request.
 *
 * @author duhongming
 * @date 2018 /8/21
 */
public class NetworkRequest extends NetworkMessage {

    /**
     * The action name of authentication request.
     */
    public static final String AUTH_ACTION_NAME = "AUTHENTICATE";

    private String actionName;
    private Address sender;

    /**
     * Instantiates a new Network request.
     *
     * @param id         the id
     * @param actionName the action name
     * @param payload    the payload
     */
    public NetworkRequest(long id, String actionName, byte[] payload) {
        super(id, payload);
        this.actionName = actionName;
    }

    /**
     * Sender network request.
     *
     * @param sender the sender
     * @return the network request
     */
    public NetworkRequest sender(Address sender) {
        this.sender = sender;
        return this;
    }

    /**
     * Sender address.
     *
     * @return the address
     */
    public Address sender() {
        return this.sender;
    }

    /**
     * Action name string.
     *
     * @return the string
     */
    public String actionName() {
        return this.actionName;
    }

    @Override
    public Type type() {
        return Type.REQUEST;
    }
}
