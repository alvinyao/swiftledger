package com.higgschain.trust.network.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Application.
 *
 * @author duhongming
 * @date 2018 /9/11
 */
@SpringBootApplication
public class Application {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Server start on 8080");
    }
}
