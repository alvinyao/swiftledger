package com.higgschain.trust.rs.core.task;

import com.higgschain.trust.rs.core.api.enums.CoreTxStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * The type Tx id producer.
 *
 * @author liuyu
 */
@Component @Slf4j public class TxIdProducer implements InitializingBean {
    private LinkedBlockingQueue<TxIdBO> queueInit = null;
    private LinkedBlockingQueue<TxIdBO> queueWait = null;
    @Value("${rs.core.schedule.queueInitSize:30000}") private int maxInitSize;
    @Value("${rs.core.schedule.queueWaitSize:10000}") private int maxWaitSize;

    @Override public void afterPropertiesSet() throws Exception {
        queueInit = new LinkedBlockingQueue<>(maxInitSize);
        queueWait = new LinkedBlockingQueue<>(maxWaitSize);
    }

    /**
     * put txId
     *
     * @param txId the tx id
     */
    public void put(TxIdBO txId) {
        try {
            if(txId.getStatusEnum() == CoreTxStatusEnum.INIT) {
                queueInit.offer(txId);
            }else if(txId.getStatusEnum() == CoreTxStatusEnum.WAIT){
                queueWait.offer(txId);
            }
        } catch (Exception e) {
            log.error("put txId has error", e);
        }
    }

    /**
     * take txId for status
     *
     * @param statusEnum the status enum
     * @return tx id bo
     */
    public TxIdBO take(CoreTxStatusEnum statusEnum) {
        try {
            if(statusEnum == CoreTxStatusEnum.INIT) {
                return queueInit.poll();
            }else if(statusEnum == CoreTxStatusEnum.WAIT){
                return queueWait.poll();
            }
        } catch (Exception e) {
            log.error("take txId has error", e);
        }
        return null;
    }

    /**
     * size for init
     *
     * @return int
     */
    public int initSize(){
        return queueInit.size();
    }

    /**
     * size for wait
     *
     * @return int
     */
    public int waitSize(){
        return queueWait.size();
    }
}
