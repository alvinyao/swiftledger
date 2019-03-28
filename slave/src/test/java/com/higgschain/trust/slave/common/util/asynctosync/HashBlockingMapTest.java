package com.higgschain.trust.slave.common.util.asynctosync;

import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.slave.common.util.asynctosync.BlockingMap;
import com.higgschain.trust.slave.common.util.asynctosync.HashBlockingMap;
import org.testng.annotations.Test;

public class HashBlockingMapTest {

    @Test public void testPut() throws Exception {
    }

    @Test public void testTake() throws Exception {
    }

    @Test public void testPoll() throws Exception {
    }

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