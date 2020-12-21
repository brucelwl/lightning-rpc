package com.lightning.rpc.example.consumer;

import com.bruce.lightning.rpc.spring.client.RpcReference;
import com.lightning.api.UserService;
import com.lightning.api.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/consumer")
public class RpcConsumerController {

    @RpcReference
    private UserService userService;

    @GetMapping("/test")
    public String test() {
        User user = new User();
        user.setId(123);
        user.setName("bruce");

        String s = userService.toString();
        String s1 = userService.saveUsers(Collections.singletonList(user));

        return s1;
    }

    @GetMapping("/test2")
    public User test2() {
        User user = new User();
        user.setId(123);
        user.setName("bruce");

        return userService.saveUser(user);
    }

    @GetMapping("/test3")
    public int test3() {
        User user = new User();
        user.setId(123);
        user.setName("bruce");
        return userService.updateUser(user, 18, "上海", true);
    }


}
