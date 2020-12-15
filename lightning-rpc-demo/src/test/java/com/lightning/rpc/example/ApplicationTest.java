package com.lightning.rpc.example;


import com.lightning.rpc.example.bean.User;
import com.lightning.rpc.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LightningRpcExampleApplication.class)
public class ApplicationTest {

    @Autowired
    private UserService userService;

    @Test
    public void test1(){

        User user = new User();
        userService.saveUser(user);

    }






}
