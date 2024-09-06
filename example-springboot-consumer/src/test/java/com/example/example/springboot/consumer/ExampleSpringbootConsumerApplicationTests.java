package com.example.example.springboot.consumer;

import com.zenith.zenith.rpc.springboot.starter.annotation.EnableRpc;
import com.zenith.zenith.rpc.springboot.starter.annotation.RpcReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@EnableRpc(needServer = false)
class ExampleSpringbootConsumerApplicationTests {
    @Autowired
    private ExampleServiceImpl exampleService;

    @Test
    void contextLoads() {
        exampleService.test();
    }

}
