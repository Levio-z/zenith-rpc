package com.zenith.example.provider;

import com.zenith.example.common.model.User;
import com.zenith.example.common.service.UserService;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements UserService {

    /**
     * 功能是打印用户的名称，并且返回参数中的用户对象
     * @param user
     * @return {@link User}
     */
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        user.setName("服务者逻辑");
        return user;
    }
}