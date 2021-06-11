package com.example.billmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BillManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillManagerApplication.class, args);
    }
}
