package com.higgschain.trust.network.message;

import java.io.Serializable;

/**
 * The type Authentication request.
 *
 * @author duhongming
 * @date 2018 /8/31
 */
public class AuthenticationRequest implements Serializable {

    private String publicKey;
    private String nodeName;
    private long nonce;
    private int httpPort;
    private String signature;
    private boolean isBackupNode;

    /**
     * Instantiates a new Authentication request.
     *
     * @param nodeName     the node name
     * @param publicKey    the public key
     * @param nonce        the nonce
     * @param httpPort     the http port
     * @param signature    the signature
     * @param isBackupNode the is backup node
     */
    public AuthenticationRequest(String nodeName, String publicKey, long nonce, int httpPort, String signature, boolean isBackupNode) {
        this.nodeName = nodeName;
        this.publicKey = publicKey;
        this.nonce = nonce;
        this.httpPort = httpPort;
        this.signature = signature;
        this.isBackupNode = isBackupNode;
    }

    /**
     * Gets public key.
     *
     * @return the public key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Sets public key.
     *
     * @param publicKey the public key
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Gets node name.
     *
     * @return the node name
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets node name.
     *
     * @param nodeName the node name
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Gets nonce.
     *
     * @return the nonce
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * Sets nonce.
     *
     * @param nonce the nonce
     */
    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    /**
     * Gets http port.
     *
     * @return the http port
     */
    public int getHttpPort() {
        return httpPort;
    }

    /**
     * Sets http port.
     *
     * @param httpPort the http port
     */
    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    /**
     * Gets signature.
     *
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets signature.
     *
     * @param signature the signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Is backup node boolean.
     *
     * @return the boolean
     */
    public boolean isBackupNode() {
        return isBackupNode;
    }

    /**
     * Sets backup node.
     *
     * @param backupNode the backup node
     */
    public void setBackupNode(boolean backupNode) {
        isBackupNode = backupNode;
    }
}
