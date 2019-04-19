package com.higgschain.trust.evmcontract;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * Test list.
     */
    @Test
    public void testList() {

        List list = new ArrayList<>();

        list.add(BigInteger.valueOf(20L));
        list.add("1111");

        list.add("2222".getBytes());
        System.out.println(list);


    }

    /**
     * Method.
     *
     * @param str the str
     * @param sb  the sb
     */
    public void method(String str,StringBuilder sb){
        str += "xxxxxxxxxxx";
        sb = new StringBuilder();
        sb.append("xxxxxxxxxxx");

    }

    /**
     * Pass by value.
     */
    @Test
    public void passByValue(){
        String str = null;
        StringBuilder sb = new StringBuilder();
        method(str,sb);

        System.out.println(str);
        System.out.println("sb"+sb.toString());
    }

    /**
     * test ExecutorService
     */
    @Test
    public void testExecutorService(){

        ExecutorService pool = new ThreadPoolExecutor(1,
                4,
                100L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactoryBuilder().setNameFormat("com.higgsblock.trie-calc-thread-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());

        pool.execute(() -> System.out.print(Thread.currentThread().getName()));
        pool.shutdown();
    }

    /**
     * Test sub string.
     */
    @Test
    public void testSubString(){
        String str = "1.8.0_111";
        int first = str.indexOf(".");
        int second = str.indexOf(".", first + 1);
        System.out.println(str.substring(0,second));
    }

    /**
     * Test.
     */
    @Test
    public void test(){
        {
            System.out.println("11111111111");
            System.out.println("222222222");
        }
    }

    @Test
    public void testHex(){
        System.out.println(Hex.toHexString("STACS".getBytes(Charsets.UTF_8)));
        System.out.println(Hex.toHexString("policy_id".getBytes(Charsets.UTF_8)));
        String hexStr = Hex.toHexString("policy_id".getBytes(Charsets.UTF_8));
        System.out.println(Strings.padStart(hexStr,64,'0'));
    }

}
