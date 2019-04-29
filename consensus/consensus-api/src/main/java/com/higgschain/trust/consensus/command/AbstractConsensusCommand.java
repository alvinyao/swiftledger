package com.higgschain.trust.consensus.command;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.consensus.util.DeflateUtil;
import lombok.ToString;

import java.util.zip.DataFormatException;

/**
 * The type Abstract consensus command.
 *
 * @param <T> the type parameter
 * @author cwy
 */
@ToString public abstract class AbstractConsensusCommand<T> implements ConsensusCommand<T> {
    private static final long serialVersionUID = 1L;
    private T value;
    private Long traceId;
    private byte[] bytes;

    /**
     * Instantiates a new Abstract consensus command.
     *
     * @param value the value
     */
    public AbstractConsensusCommand(T value) {
        this.value = value;
    }

    /**
     * Instantiates a new Abstract consensus command.
     *
     * @param bytes the bytes
     */
    public AbstractConsensusCommand(byte[] bytes){
        this.bytes = bytes;
    }



    @Override public T get() {
        return this.value;
    }

    /**
     * Get value bytes byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getValueBytes() {
        return this.bytes;
    }

    /**
     * Gets value from byte.
     *
     * @param clazz the clazz
     * @return the value from byte
     * @throws DataFormatException the data format exception
     */
    public T getValueFromByte(Class<T> clazz) throws DataFormatException {
        byte[] decom = DeflateUtil.uncompress(bytes);
        T result = JSON.parseObject(new String(decom),clazz);
        return result;
    }

    /**
     * Gets trace id.
     *
     * @return the trace id
     */
    public Long getTraceId() {
        return traceId;
    }

    /**
     * Sets trace id.
     *
     * @param traceId the trace id
     */
    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

}
