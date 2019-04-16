package com.higgschain.trust.press;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.presstest.vo.StoreVO;

import java.io.IOException;
import java.util.Random;

/**
 * The type Store test.
 *
 * @author liuyu
 * @description
 * @date 2018 -09-03
 */
public class StoreTest extends BasePressTest{

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
          test();
    }
    /**
     * 测试
     *
     * @throws IOException
     */
    private static void test() throws IOException {
        for (int i = 0; i < 800; i++) {
            new Thread(new MyTask()).start();
        }
        //wait
        System.in.read();
    }

    /**
     * task
     */
    static class MyTask implements Runnable {
        @Override public void run() {
            StoreTest accountTest = new StoreTest();
            while (true) {
                accountTest.store();
            }
        }
    }

    /**
     * 存证
     */
    public void store() {
        for (int i = 0; i < 8; i++) {
            StoreVO vo = new StoreVO();
            vo.setReqNo("tx_id_store_" + i + "_" + System.currentTimeMillis() + new Random().nextInt(10000) + "-" + Thread.currentThread().getName());
            vo.setValues(new String[]{"store-value-" + i,"aaa-" + i,"xxx-" + i,"bbb-" + i,"vvvv-" + i});
            send("press/store", JSON.toJSONString(vo));
        }
    }
}
