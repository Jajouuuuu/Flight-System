package com.flight_system.check_in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CheckInApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckInApplication.class, args);
    }
} 