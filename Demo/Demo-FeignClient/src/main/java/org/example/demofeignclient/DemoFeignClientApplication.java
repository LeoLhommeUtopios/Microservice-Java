package org.example.demofeignclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoFeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoFeignClientApplication.class, args);
    }

}
