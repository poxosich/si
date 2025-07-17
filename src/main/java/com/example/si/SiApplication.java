package com.example.si;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class SiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiApplication.class, args);
    }

}
