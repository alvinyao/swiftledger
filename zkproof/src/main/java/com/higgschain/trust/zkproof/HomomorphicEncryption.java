package com.higgschain.trust.zkproof;

import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;

/**
 * The interface Homomorphic encryption.
 */
public interface HomomorphicEncryption {

    /**
     * The enum Keystat.
     */
    enum  KEYSTAT{/**
     * Has no key keystat.
     */
    hasNoKey("0","不具有合法的公私钥"),
        /**
         * Has pub key keystat.
         */
        hasPubKey("1","具有一个可能合法的公钥"),
        /**
         * Has full key keystat.
         */
        hasFullKey("2","具有一套合法的公私钥");

        private String code;
        private String msg;

        KEYSTAT(String code,String msg)
        {
            this.code = code;
            this.msg = msg;
        }

        /**
         * Gets code.
         *
         * @return the code
         */
        public String getCode()
        {
            return  this.code;
        }
    }

    /**
     * Export full key string.
     *
     * @return the string
     */
    String exportFullKey();

    /**
     * Export pub key string.
     *
     * @return the string
     */
    String exportPubKey();

    /**
     * Has full key boolean.
     *
     * @return the boolean
     */
    boolean hasFullKey();

    /**
     * Has pub key boolean.
     *
     * @return the boolean
     */
    boolean hasPubKey();

    /**
     * Cipher add string.
     *
     * @param em1 the em 1
     * @param em2 the em 2
     * @return the string
     */
    String cipherAdd(String em1, String em2);

    /**
     * Decryption big integer.
     *
     * @param em the em
     * @return the big integer
     */
    BigInteger Decryption(String em);

    /**
     * Encryption string.
     *
     * @param b the b
     * @param r the r
     * @return the string
     */
    String Encryption(BigInteger b, BigInteger r);

    /**
     * Too big boolean.
     *
     * @param b the b
     * @return the boolean
     */
    boolean tooBig(BigInteger b);

    /**
     * Too big random boolean.
     *
     * @param r the r
     * @return the boolean
     */
    boolean tooBigRandom(BigInteger r);

    /**
     * Gen sub key string.
     *
     * @param key     the key
     * @param seqno   the seqno
     * @param nodeNum the node num
     * @return the string
     */
    static String GenSubKey(String key, int seqno, int nodeNum){
        JSONObject ob = JSONObject.parseObject(key);
        if (ob.getString("key_type").compareTo("BGN") == 0){
            return BGNKey.GenSubKey(key, seqno, nodeNum);
        }
        return null;
    }

    /**
     * Merge key string.
     *
     * @param key1 the key 1
     * @param key2 the key 2
     * @return the string
     */
    static String MergeKey(String key1, String key2){
        if(key2 == null||key2.length()==0){
            return key1;
        }
        JSONObject ob1 = JSONObject.parseObject(key1);
        JSONObject ob2 = JSONObject.parseObject(key2);
        if (ob1.getString("key_type").compareTo("BGN") == 0
                &&ob2.getString("key_type").compareTo("BGN") == 0){
           return BGNKey.MergeKey(key1, key2);
        }
        return null;
    }

    /**
     * Contain key boolean.
     *
     * @param fullKey the full key
     * @param subKey  the sub key
     * @return the boolean
     */
    static boolean ContainKey(String fullKey, String subKey){
        JSONObject ob1 = JSONObject.parseObject(fullKey);
        JSONObject ob2 = JSONObject.parseObject(subKey);
        if (ob1.getString("key_type").compareTo("BGN") == 0
                &&ob2.getString("key_type").compareTo("BGN") == 0){
            return BGNKey.ContainKey(fullKey, subKey);
        }
        return false;
    }


}
