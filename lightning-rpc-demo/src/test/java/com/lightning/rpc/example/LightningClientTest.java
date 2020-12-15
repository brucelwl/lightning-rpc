package com.lightning.rpc.example;

import com.bruce.lightning.rpc.client.DefaultResponseFuture;
import com.bruce.lightning.rpc.client.LightningClient;
import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.common.RpcResponse;
import com.bruce.lightning.rpc.util.JsonUtils;
import com.lightning.rpc.example.bean.User;
import com.lightning.rpc.example.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class LightningClientTest {

    @Test
    public void test1() {
        LightningClient client = new LightningClient();
        client.startSync("127.0.0.1", 8008);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setId(1L);
        rpcRequest.setFullMethodName("com.bruce.use.service.UserService.saveUsers");

        User user = new User();
        user.setId(121);
        user.setName("hahaha 中文");

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);

        rpcRequest.setArgs(new Object[]{users});

        DefaultResponseFuture responseFuture = client.send(rpcRequest);
        RpcResponse response = responseFuture.get();

        System.out.println(JsonUtils.toJson(response));
    }


    @Test
    public void test2() {
        LightningClient client = new LightningClient();
        client.startSync("127.0.0.1", 8008);
        UserService userService = client.createProxy(UserService.class, 3000);

        User user = new User();
        user.setName("bruce");

        userService.saveUser(user);
    }


}
