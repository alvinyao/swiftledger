package com.higgschain.trust.slave.common.util.asynctosync;

import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.slave.common.util.asynctosync.BlockingMap;
import com.higgschain.trust.slave.common.util.asynctosync.HashBlockingMap;
import org.testng.annotations.Test;

/**
 * The type Hash blocking map test.
 */
public class HashBlockingMapTest {

    /**
     * Test put.
     *
     * @throws Exception the exception
     */
    @Test public void testPut() throws Exception {
    }

    /**
     * Test take.
     *
     * @throws Exception the exception
     */
    @Test public void testTake() throws Exception {
    }

    /**
     * Test poll.
     *
     * @throws Exception the exception
     */
    @Test public void testPoll() throws Exception {
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        BlockingMap<String> hashBlockingMap = new HashBlockingMap<String>(Constant.MAX_BLOCKING_QUEUE_SIZE);

        hashBlockingMap.put("123", "test");

        long start = System.currentTimeMillis();
        String str =  hashBlockingMap.poll("123", 1);

        System.out.println(System.currentTimeMillis() - start);
        System.out.println(str);

        System.out.println(hashBlockingMap.poll("123", 2000));
    }
}