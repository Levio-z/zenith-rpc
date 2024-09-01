package com.zenith.zenithrpc.config;

import com.zenith.zenithrpc.loadbalancer.LoadBalancerKeys;
import com.zenith.zenithrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC 框架配置
 *
 * @author zenith
 * @date 2024/07/27
 */
@Data
public class RpcConfig {
    /**
     * 名词
     */
    private String name = "zenith-rpc";
    /**
     * 版本号
     */
    private String version = "1.0";
    /**
     * 服务器主机名
     */
    private String host = "localhost";
    /**
     * 端口
     */
    private Integer serverPort = 8082;
    /**
     * 模拟调用
     */
    private boolean mock = false;
    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 负载均衡
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
}
