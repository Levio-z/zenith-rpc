package com.zenith.example.provider;


import com.zenith.example.common.service.UserService;
import com.zenith.zenithrpc.RpcApplication;
import com.zenith.zenithrpc.registry.LocalRegistry;
import com.zenith.zenithrpc.server.VertxHttpServer;

/**
 * 简单服务提供者示例
 */
public class Provider {
    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        //提供服务
        com.zenith.zenithrpc.server.HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
