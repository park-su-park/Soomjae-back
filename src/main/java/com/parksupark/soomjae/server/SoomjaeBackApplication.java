package com.parksupark.soomjae.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SoomjaeBackApplication {

    public static void main(String[] args) {
        //hello
        SpringApplication.run(SoomjaeBackApplication.class, args);
    }

}
