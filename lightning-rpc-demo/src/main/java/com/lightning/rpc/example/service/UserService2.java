package com.lightning.rpc.example.service;


import com.lightning.rpc.example.bean.User;

import java.util.List;

//@ExcludeRpc
public interface UserService2 {

    String saveUser(User user);

    public String saveUsers(List<User> user);


}
