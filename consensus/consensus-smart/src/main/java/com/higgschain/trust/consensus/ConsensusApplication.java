package com.higgschain.trust.consensus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Consensus application.
 *
 * @author ShenTeng
 */
@SpringBootApplication(scanBasePackages = "com.higgschain.trust.consensus") public class ConsensusApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ConsensusApplication.class, args);
    }

}
