package com.example.example.springboot.consumer;

import com.zenith.example.common.model.User;
import com.zenith.example.common.service.UserService;
import com.zenith.zenith.rpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {
    @RpcReference
    private UserService userService;

    public void test(){
        User user = new User();
        user.setName("zzz");
        User resultUser =userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
