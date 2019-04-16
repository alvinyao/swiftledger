package com.higgschain.trust.network.codec;

/**
 * The interface Serializable.
 *
 * @author duhongming
 * @date 2018 /8/22
 */
public interface Serializable {
    /**
     * 序列化
     *
     * @return byte [ ]
     */
    byte[] serialize();

    /**
     * 反序列化
     *
     * @param data the data
     */
    void deserialize(byte[] data);
}
