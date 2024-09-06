package com.zenith.example.provider;

import cn.hutool.core.net.NetUtil;
import com.zenith.example.common.service.UserService;
import com.zenith.zenithrpc.RpcApplication;
import com.zenith.zenithrpc.bootstrap.ProviderBootstrap;
import com.zenith.zenithrpc.config.RegistryConfig;
import com.zenith.zenithrpc.config.RpcConfig;
import com.zenith.zenithrpc.model.ServiceMetaInfo;
import com.zenith.zenithrpc.model.ServiceRegisterInfo;
import com.zenith.zenithrpc.registry.EtcdRegistry;
import com.zenith.zenithrpc.registry.LocalRegistry;
import com.zenith.zenithrpc.registry.Registry;
import com.zenith.zenithrpc.registry.RegistryFactory;
import com.zenith.zenithrpc.server.HttpServer;
import com.zenith.zenithrpc.server.VertxHttpServer;
import com.zenith.zenithrpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;


/**
 * 得到标记类型列表形式
 *
 * @author zenith
 * @date 2024/08/23
 */
public class ProviderExample {

    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}