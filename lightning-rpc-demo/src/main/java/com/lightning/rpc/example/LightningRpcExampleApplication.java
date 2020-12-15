package com.lightning.rpc.example;

import com.bruce.lightning.rpc.spring.client.EnableNettyRpcClient;
import com.bruce.lightning.rpc.spring.server.EnableNettyRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableNettyRpcClient(host = "127.0.0.1",port = 8008)
@EnableNettyRpcServer(port = 8008)
@SpringBootApplication
public class LightningRpcExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightningRpcExampleApplication.class, args);


    }
}
