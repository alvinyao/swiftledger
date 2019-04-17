package com.higgschain.trust.slave.core.spi;

import com.higgschain.trust.evmcontract.spi.IShout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tangkun
 * @date 2019-04-17
 */
@Slf4j
@Service("dogBean")
public class Dog implements IShout {

    public Dog(){
        log.info("dog bean init");
    }
    /**
     *
     */
    @Override
    public void shot() {
      log.info("dog shot");
    }
}
