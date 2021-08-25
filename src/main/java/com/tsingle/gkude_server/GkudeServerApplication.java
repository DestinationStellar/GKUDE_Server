package com.tsingle.gkude_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GkudeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GkudeServerApplication.class, args);
    }

}
