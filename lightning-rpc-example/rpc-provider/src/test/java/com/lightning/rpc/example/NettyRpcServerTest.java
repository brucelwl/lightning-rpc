package com.lightning.rpc.example;

import com.bruce.lightning.rpc.server.LightningServer;
import com.bruce.lightning.rpc.server.mapping.BeanMethodContext;
import com.lightning.rpc.example.service.UserServiceImpl;

public class NettyRpcServerTest {

    public static void main(String[] args) {

        UserServiceImpl userService = new UserServiceImpl();

        BeanMethodContext.addServiceBean(userService);


        LightningServer lightningServer = new LightningServer(8008);

        lightningServer.startSync();

        System.out.println("服务启动");



    }



}
