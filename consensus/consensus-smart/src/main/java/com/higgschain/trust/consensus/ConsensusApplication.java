package com.higgschain.trust.consensus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ShenTeng
 */
@SpringBootApplication(scanBasePackages = "com.higgschain.trust.consensus") public class ConsensusApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsensusApplication.class, args);
    }

}
