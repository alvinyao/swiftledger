package com.higgschain.trust.network.springboot;

import com.higgschain.trust.network.NetworkManage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Person bean.
 *
 * @author duhongming
 * @date 2018 /9/12
 */
@Service
public class PersonBean implements InitializingBean {
    /**
     * The Network manage.
     */
    @Autowired
    NetworkManage networkManage;
    /**
     * The Context aware.
     */
    @Autowired
    ContextAware contextAware;

    /**
     * The Port.
     */
    public int port;

    @Override
    public void afterPropertiesSet() throws Exception {
//        System.out.println(Thread.currentThread().toString());
//        Arrays.stream(Thread.currentThread().getStackTrace()).forEach(System.out::println);
        System.out.println("PersonBean InitializingBean afterPropertiesSet..." + port);
    }
}
