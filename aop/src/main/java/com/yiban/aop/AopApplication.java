package com.yiban.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
//        new File("logs").mkdirs();
        SpringApplication.run(AopApplication.class, args);
    }

}
