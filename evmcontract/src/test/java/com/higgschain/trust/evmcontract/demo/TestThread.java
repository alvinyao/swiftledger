package com.higgschain.trust.evmcontract.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @author tangkun
 * @date 2019-04-18
 */
public class TestThread {
    private static Object lock = new Object();
    static     A a = new A();
    public static void main(String[] args) throws InterruptedException {

            new  Thread(() -> {
                //while (a.getName() == null){
                //synchronized (lock){}
                //}
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                a.setName("123");
                while ( a.getName().equals("123")) {

                }

            }).start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("main get name :"+a.getName());
        a.setName("hello world");
        System.out.println("main finish");
    }

    public static void sayHello(String msg){
        System.out.print(msg);
    }


}
class A{
    @Setter
    @Getter
    private String name;

}