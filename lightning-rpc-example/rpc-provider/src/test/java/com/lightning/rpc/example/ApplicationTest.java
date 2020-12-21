package com.lightning.rpc.example;


import com.lightning.api.UserService;
import com.lightning.api.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LightningProviderApplication.class)
public class ApplicationTest {

    @Autowired
    private UserService userService;

    @Test
    public void test1(){

        User user = new User();
        userService.saveUser(user);

    }






}
