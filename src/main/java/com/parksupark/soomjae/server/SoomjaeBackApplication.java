package com.parksupark.soomjae.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SoomjaeBackApplication {

    public static void main(String[] args) {
        //hello
        SpringApplication.run(SoomjaeBackApplication.class, args);
    }

}
