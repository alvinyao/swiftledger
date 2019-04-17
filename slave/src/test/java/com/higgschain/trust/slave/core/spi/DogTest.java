package com.higgschain.trust.slave.core.spi;

import com.higgschain.trust.evmcontract.spi.IShout;
import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

/**
 * @author tangkun
 * @date 2019-04-17
 */
@Slf4j
public class DogTest extends BaseTest{

    @Autowired
    Dog dogBean;

    @Test
    public void testShot() throws Exception {

        dogBean.shot();
    }
}