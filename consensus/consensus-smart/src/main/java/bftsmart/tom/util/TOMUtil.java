/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and the authors indicated in the @author tags
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bftsmart.tom.util;

import bftsmart.reconfiguration.ViewController;

import java.io.*;
import java.security.*;
import java.util.Arrays;

/**
 * The type Tom util.
 */
public class TOMUtil {

    //private static final int BENCHMARK_PERIOD = 10000;

    /**
     * The constant RR_REQUEST.
     */
    //some message types
    public static final int RR_REQUEST = 0;
    /**
     * The constant RR_REPLY.
     */
    public static final int RR_REPLY = 1;
    /**
     * The constant RR_DELIVERED.
     */
    public static final int RR_DELIVERED = 2;
    /**
     * The constant STOP.
     */
    public static final int STOP = 3;
    /**
     * The constant STOPDATA.
     */
    public static final int STOPDATA = 4;
    /**
     * The constant SYNC.
     */
    public static final int SYNC = 5;
    /**
     * The constant SM_REQUEST.
     */
    public static final int SM_REQUEST = 6;
    /**
     * The constant SM_REPLY.
     */
    public static final int SM_REPLY = 7;
    /**
     * The constant SM_ASK_INITIAL.
     */
    public static final int SM_ASK_INITIAL = 11;
    /**
     * The constant SM_REPLY_INITIAL.
     */
    public static final int SM_REPLY_INITIAL = 12;

    /**
     * The constant TRIGGER_LC_LOCALLY.
     */
    public static final int TRIGGER_LC_LOCALLY = 8;
    /**
     * The constant TRIGGER_SM_LOCALLY.
     */
    public static final int TRIGGER_SM_LOCALLY = 9;

    private static int signatureSize = -1;

    /**
     * Gets signature size.
     *
     * @param controller the controller
     * @return the signature size
     */
    public static int getSignatureSize(ViewController controller) {
        if (signatureSize > 0) {
            return signatureSize;
        }

        byte[] signature = signMessage(controller.getStaticConf().getRSAPrivateKey(), "a".getBytes());

        if (signature != null) {
            signatureSize = signature.length;
        }

        return signatureSize;
    }

    /**
     * Get bytes byte [ ].
     *
     * @param o the o
     * @return the byte [ ]
     */
    //******* EDUARDO BEGIN **************//
    public static byte[] getBytes(Object o) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream obOut = null;
        try {
            obOut = new ObjectOutputStream(bOut);
            obOut.writeObject(o);
            obOut.flush();
            bOut.flush();
            obOut.close();
            bOut.close();
        } catch (IOException ex) {
            Logger.printError(ex.getMessage(), ex);
            return null;
        }

        return bOut.toByteArray();
    }

    /**
     * Gets object.
     *
     * @param b the b
     * @return the object
     */
    public static Object getObject(byte[] b) {
        if (b == null)
            return null;

        ByteArrayInputStream bInp = new ByteArrayInputStream(b);
        try {
            ObjectInputStream obInp = new ObjectInputStream(bInp);
            Object ret = obInp.readObject();
            obInp.close();
            bInp.close();
            return ret;
        } catch (Exception ex) {
            return null;
        }
    }
    //******* EDUARDO END **************//

    /**
     * Sign a message.
     *
     * @param key     the private key to be used to generate the signature
     * @param message the message to be signed
     * @return the signature
     */
    public static byte[] signMessage(PrivateKey key, byte[] message) {

        byte[] result = null;
        try {

            Signature signatureEngine = Signature.getInstance("SHA1withRSA");

            signatureEngine.initSign(key);

            signatureEngine.update(message);

            result = signatureEngine.sign();
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Verify the signature of a message.
     *
     * @param key       the public key to be used to verify the signature
     * @param message   the signed message
     * @param signature the signature to be verified
     * @return true if the signature is valid, false otherwise
     */
    public static boolean verifySignature(PublicKey key, byte[] message, byte[] signature) {

        boolean result = false;

        try {
            Signature signatureEngine = Signature.getInstance("SHA1withRSA");

            signatureEngine.initVerify(key);

            result = verifySignature(signatureEngine, message, signature);
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Verify the signature of a message.
     *
     * @param initializedSignatureEngine a signature engine already initialized                                   for verification
     * @param message                    the signed message
     * @param signature                  the signature to be verified
     * @return true if the signature is valid, false otherwise
     * @throws SignatureException the signature exception
     */
    public static boolean verifySignature(Signature initializedSignatureEngine, byte[] message, byte[] signature)
        throws SignatureException {

        initializedSignatureEngine.update(message);
        return initializedSignatureEngine.verify(signature);
    }

    /**
     * Byte array to string string.
     *
     * @param b the b
     * @return the string
     */
    public static String byteArrayToString(byte[] b) {
        String s = "";
        for (int i = 0; i < b.length; i++) {
            s = s + b[i];
        }

        return s;
    }

    /**
     * Equals hash boolean.
     *
     * @param h1 the h 1
     * @param h2 the h 2
     * @return the boolean
     */
    public static boolean equalsHash(byte[] h1, byte[] h2) {
        return Arrays.equals(h2, h2);
    }

    /**
     * Compute hash byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public static final byte[] computeHash(byte[] data) {

        byte[] result = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = md.digest(data);

        } catch (NoSuchAlgorithmException e) {
            Logger.printError(e.getMessage(), e);
        } // TODO: shouldn't it be SHA?

        return result;
    }

}
