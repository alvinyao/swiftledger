package com.higgschain.trust.evmcontract.demo;

import lombok.Setter;

/**
 * @author tangkun
 * @date 2019-04-19
 */
public class JvmCodeTest {

    public static void main(String[] args) {


        int a = 10;

        B b = new B();
        b.setName("hello world");
    }
}

class B{
    @Setter
    private String name;
}
