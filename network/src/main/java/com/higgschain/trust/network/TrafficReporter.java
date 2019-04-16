package com.higgschain.trust.network;

/**
 * The interface Traffic reporter.
 *
 * @author duhongming
 * @date 2018 /12/27
 */
public interface TrafficReporter {

    /**
     * 入站流量
     *
     * @param bytes the bytes
     */
    void inbound(long bytes);

    /**
     * 出站流量
     *
     * @param bytes the bytes
     */
    void outbound(long bytes);

    /**
     * The constant Default.
     */
    TrafficReporter  Default = new TrafficReporter() {
        @Override
        public void inbound(long bytes) {
        }

        @Override
        public void outbound(long bytes) {
        }
    };
}
