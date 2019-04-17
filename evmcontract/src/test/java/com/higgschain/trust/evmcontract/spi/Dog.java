package com.higgschain.trust.evmcontract.spi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tangkun
 * @date 2019-04-17
 */
@Slf4j
public class Dog implements IShout {
    /**
     *
     */
    @Override
    public void shot() {
        System.out.println("xxxxxxxxxxxxx");
      log.info("dog shot");
    }
}
