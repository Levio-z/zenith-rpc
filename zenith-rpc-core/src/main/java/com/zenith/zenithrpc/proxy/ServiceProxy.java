package com.zenith.zenithrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zenith.zenithrpc.RpcApplication;
import com.zenith.zenithrpc.config.RpcConfig;
import com.zenith.zenithrpc.constant.RpcConstant;
import com.zenith.zenithrpc.fault.retry.RetryStrategy;
import com.zenith.zenithrpc.fault.retry.RetryStrategyFactory;
import com.zenith.zenithrpc.loadbalancer.LoadBalancer;
import com.zenith.zenithrpc.loadbalancer.LoadBalancerFactory;
import com.zenith.zenithrpc.model.ServiceMetaInfo;
import com.zenith.zenithrpc.protocol.*;
import com.zenith.zenithrpc.registry.Registry;
import com.zenith.zenithrpc.registry.RegistryFactory;
import com.zenith.zenithrpc.serializer.JdkSerializer;
import com.zenith.zenithrpc.serializer.Serializer;
import com.zenith.zenithrpc.model.RpcRequest;
import com.zenith.zenithrpc.model.RpcResponse;
import com.zenith.zenithrpc.serializer.SerializerFactory;
import com.zenith.zenithrpc.server.tcp.ProtocolMessageDecoder;
import com.zenith.zenithrpc.server.tcp.ProtocolMessageEncoder;
import com.zenith.zenithrpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 *
 * @author zenith
 * @date 2024/07/26
 */
public class ServiceProxy implements InvocationHandler {



    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {

        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        // 发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();


        try {
            // 从注册中心获取服务提供者地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }

            LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(rpcConfig.getLoadBalancer());
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.defaultSelectService(requestParams, serviceMetaInfoList);
            // 使用重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
            return rpcResponse.getData();
        } catch (Exception  e) {
            e.printStackTrace();
            throw new RuntimeException("调用失败");
        }

    }
}
