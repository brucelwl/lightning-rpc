package com.lightning.rpc.example;

import com.bruce.lightning.rpc.spring.client.EnableNettyRpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableNettyRpcClient(host = "127.0.0.1",port = 8008)
@SpringBootApplication
public class LightningConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightningConsumerApplication.class, args);


    }
}
