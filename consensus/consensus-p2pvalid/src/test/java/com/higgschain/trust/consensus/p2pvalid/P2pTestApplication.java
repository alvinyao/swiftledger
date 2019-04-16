package com.higgschain.trust.consensus.p2pvalid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * The type P 2 p test application.
 *
 * @author cwy
 */
@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableDiscoveryClient
//@EnableFeignClients
@ComponentScan({"com.higgschain.trust.consensus.p2pvalid", "com.higgschain.trust.common"})
public class P2pTestApplication {
}