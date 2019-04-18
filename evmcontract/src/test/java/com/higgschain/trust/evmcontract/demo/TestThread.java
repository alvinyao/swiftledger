package com.higgschain.trust.evmcontract.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @author tangkun
 * @date 2019-04-18
 */
public class TestThread {

    static     A a = new A();
    public static void main(String[] args) throws InterruptedException {

            new  Thread(() -> {
                while (a.getName() == null){
                    System.out.println("hello world");
                }
            }).start();

        TimeUnit.SECONDS.sleep(1);
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