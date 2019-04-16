package com.higgschain.trust.network;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * The type Address.
 *
 * @author duhongming
 * @date 2018 /8/21
 */
public final class Address implements Serializable {
    private static final int DEFAULT_PORT = 7070;

    private int port;
    private String host;

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Local address.
     *
     * @return the address
     */
    public static Address local() {
        return from(DEFAULT_PORT);
    }

    /**
     * Instantiates a new Address.
     *
     * @param host the host
     * @param port the port
     */
    public Address(String host, int port) {
        if (host == null || host.trim().equals("")) {
            try {
                InetAddress inetAddress = getLocalAddress();
                this.host = inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("Failed to locate host", e);
            }
        } else {
            this.host = host;
        }
        this.port = port;
    }

    /**
     * From address.
     *
     * @param port the port
     * @return the address
     */
    public static Address from(int port) {
        try {
            InetAddress inetAddress = getLocalAddress();
            return new Address(inetAddress.getHostAddress(), port);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Failed to locate host", e);
        }
    }

    private static InetAddress getLocalAddress() throws UnknownHostException {
        try {
            return InetAddress.getLocalHost();
        } catch (Exception ignore) {
            return InetAddress.getByName(null);
        }
    }

    @Override
    public String toString() {
        return String.format("%s:%s", host, port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Address that = (Address) obj;
        return this.port == that.port && Objects.equals(this.host, that.host);
    }
}
