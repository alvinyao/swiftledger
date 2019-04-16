package com.higgschain.trust.common.utils;

import org.springframework.util.Base64Utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Rsa key generator utils.
 *
 * @author WangQuanzhou
 * @desc key generator util
 * @date 2018 /6/4 17:27
 */
public class RsaKeyGeneratorUtils {
    /**
     * The constant KEY_ALGORITHM.
     */
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * The constant PUB_KEY.
     */
    public static final String PUB_KEY = "pubKey";
    /**
     * The constant PRI_KEY.
     */
    public static final String PRI_KEY = "priKey";

    /**
     * Generate key pair map.
     *
     * @return the map
     */
    public static Map generateKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no such algorithm exeception", e);
        }
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        String pubKey = Base64Utils.encodeToString(publicKey.getEncoded());
        String priKey = Base64Utils.encodeToString(privateKey.getEncoded());

        Map map = new HashMap();
        map.put(PUB_KEY, pubKey);
        map.put(PRI_KEY, priKey);
        return map;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        generateKeyPair();
    }
}
