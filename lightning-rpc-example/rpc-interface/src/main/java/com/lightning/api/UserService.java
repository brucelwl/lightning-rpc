package com.lightning.api;



import com.lightning.api.bean.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    String saveUsers(List<User> user);

    int updateUser(User user, int age, String address, Boolean enable);

    Integer updateUsers(List<User> user);

}
