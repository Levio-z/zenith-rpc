package com.example.example.springboot.provider;

import com.example.example.springboot.consumer.ExampleServiceImpl;
import com.zenith.zenith.rpc.springboot.starter.annotation.EnableRpc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@EnableRpc(needServer=true)
class ExampleSpringbootProviderApplicationTests {
    @Resource
    private ExampleServiceImpl exampleService;

    @Test
    void contextLoads() {
        exampleService.test();
    }

}
