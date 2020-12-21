package com.lightning.rpc.example;

import com.bruce.lightning.rpc.spring.server.EnableNettyRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableNettyRpcServer(port = 8008)
@SpringBootApplication
public class LightningProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightningProviderApplication.class, args);


    }
}
