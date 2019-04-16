package com.higgschain.trust.network.springboot;

import com.higgschain.trust.network.NetworkManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Demo controller.
 *
 * @author duhongming
 * @date 2018 /9/11
 */
@RestController
@RequestMapping(path = "/demo")
public class DemoController {
//    @Autowired
//    NetworkManage networkManage;

    /**
     * The Person bean.
     */
    @Autowired
    PersonBean personBean;

    /**
     * Instantiates a new Demo controller.
     */
    public DemoController() {
        System.out.println("DemoController ...");
    }

    /**
     * Hello string.
     *
     * @return the string
     */
    @GetMapping(path = "/hello")
    public String hello() {
        NetworkManage.getInstance().start();
        return NetworkManage.getInstance().config().nodeName() +
                " " + personBean.toString() + " " + ContextAware.port ;
    }
}
