package com.higgschain.trust.evmcontract.vm;

import com.higgschain.trust.evmcontract.vm.DataWord;
import org.junit.Test;

/**
 * The type Data word test.
 *
 * @author tangkun
 * @date 2018 -11-21
 */
public class DataWordTest {

    /**
     * Test of.
     *
     * @throws Exception the exception
     */
    @Test
    public void testOf() throws Exception {

        long time = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            //new DataWord(32);
            DataWord.of(32);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - time));
    }
}