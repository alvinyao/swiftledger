package com.higgschain.trust.slave.common.listener;

import com.higgschain.trust.evmcontract.core.TransactionResultInfo;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Composite trust listener.
 *
 * @author duhongming
 * @date 2018 /12/17
 */
public class CompositeTrustListener implements TrustListener {

    private static abstract class RunnableInfo implements Runnable {
        private TrustListener listener;
        private String info;

        /**
         * Instantiates a new Runnable info.
         *
         * @param listener the listener
         * @param info     the info
         */
        public RunnableInfo(TrustListener listener, String info) {
            this.listener = listener;
            this.info = info;
        }

        @Override
        public String toString() {
            return "RunnableInfo: " + info + " [listener: " + listener.getClass() + "]";
        }
    }

    /**
     * The Event dispatch thread.
     */
    @Autowired
    EventDispatchThread eventDispatchThread = EventDispatchThread.getDefault();

    /**
     * The Listeners.
     */
    protected List<TrustListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(TrustListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(TrustListener listener) {
        listeners.remove(listener);
    }


    @Override
    public void onBlock(BlockHeader header) {
        for (final TrustListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onBlock") {
                @Override
                public void run() {
                    listener.onBlock(header);
                }
            });
        }
    }

    @Override
    public void onTransactionExecuted(TransactionResultInfo resultInfo) {
        for (final TrustListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onBlock") {
                @Override
                public void run() {
                    listener.onTransactionExecuted(resultInfo);
                }
            });
        }
    }
}
