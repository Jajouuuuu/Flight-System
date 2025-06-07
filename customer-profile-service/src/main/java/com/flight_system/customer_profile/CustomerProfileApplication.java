package com.flight_system.customer_profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CustomerProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerProfileApplication.class, args);
    }

} 