package com.higgschain.trust.common.crypto;

/**
 * The interface Crypto.
 */
public interface Crypto {

    /**
     * Generate key pair key pair.
     *
     * @param
     * @return key pair
     * @desc generate pub/pri key pair
     */
    KeyPair generateKeyPair();

    /**
     * Encrypt string.
     *
     * @param input     source to be encrypted
     * @param publicKey the public key
     * @return String string
     * @throws Exception the exception
     * @desc encrypt, suport ECC, SM2, RSA encrypt
     */
    String encrypt(String input, String publicKey) throws Exception;

    /**
     * Decrypt string.
     *
     * @param input      the input
     * @param privateKey the private key
     * @return String string
     * @throws Exception the exception
     * @desc decrypt, suport ECC, SM2, RSA decrypt
     */
    String decrypt(String input, String privateKey) throws Exception;

    /**
     * Sign string.
     *
     * @param message    the message
     * @param privateKey the private key
     * @return string
     * @desc sign message
     */
    String sign(String message, String privateKey);

    /**
     * Verify boolean.
     *
     * @param message   the message
     * @param signature the signature
     * @param publicKey the public key
     * @return boolean
     * @desc verify signature
     */
    boolean verify(String message, String signature, String publicKey);

}
