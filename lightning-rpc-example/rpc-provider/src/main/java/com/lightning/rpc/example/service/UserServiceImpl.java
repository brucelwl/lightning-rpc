package com.lightning.rpc.example.service;

import com.bruce.lightning.rpc.server.RpcContext;
import com.bruce.lightning.rpc.spring.server.RpcService;
import com.lightning.api.UserService;
import com.lightning.api.bean.User;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User saveUser(User user) {
        ChannelHandlerContext channelHandlerContext = RpcContext.get();


        log.info("保存用户信息");

        user.setName("hahah");
        return user;
    }

    @Override
    public String saveUsers(List<User> user) {
        log.info("保存用户信息");
        return "保存用户信息成功";
    }

    @Override
    public int updateUser(User user, int age, String address, Boolean enable) {
        log.info("更新用户信息");
        return 1;
    }


    @Override
    public Integer updateUsers(List<User> user) {
        log.info("更新用户信息");
        return null;
    }


}
