package com.higgschain.trust.slave.api;

import org.springframework.stereotype.Repository;

/**
 * The type Slave callback registor.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-13
 */
@Repository public class SlaveCallbackRegistor {
    private SlaveCallbackHandler slaveCallbackHandler;
    private SlaveBatchCallbackHandler slaveBatchCallbackHandler;

    /**
     * Regist callback handler.
     *
     * @param callbackHandler the callback handler
     */
    public void registCallbackHandler(SlaveCallbackHandler callbackHandler) {
        this.slaveCallbackHandler = callbackHandler;
    }

    /**
     * Regist batch callback handler.
     *
     * @param callbackHandler the callback handler
     */
    public void registBatchCallbackHandler(SlaveBatchCallbackHandler callbackHandler) {
        this.slaveBatchCallbackHandler = callbackHandler;
    }

    /**
     * Gets slave callback handler.
     *
     * @return the slave callback handler
     */
    public SlaveCallbackHandler getSlaveCallbackHandler() {
        return slaveCallbackHandler;
    }

    /**
     * Gets slave batch callback handler.
     *
     * @return the slave batch callback handler
     */
    public SlaveBatchCallbackHandler getSlaveBatchCallbackHandler() {
        return slaveBatchCallbackHandler;
    }
}
