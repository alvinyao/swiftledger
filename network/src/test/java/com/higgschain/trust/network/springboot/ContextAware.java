package com.higgschain.trust.network.springboot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * The type Context aware.
 *
 * @author duhongming
 * @date 2018 /9/12
 */
@Component
public class ContextAware implements ApplicationContextAware {
    /**
     * The Person bean.
     */
    @Autowired
    PersonBean personBean;

    /**
     * The constant port.
     */
    public static int port = 1;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware ...");
        System.out.println(personBean);
        System.out.println(applicationContext.getBean(PersonBean.class));
    }
}
