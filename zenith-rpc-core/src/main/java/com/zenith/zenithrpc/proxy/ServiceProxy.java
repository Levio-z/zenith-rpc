package com.zenith.zenithrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zenith.zenithrpc.RpcApplication;
import com.zenith.zenithrpc.config.RpcConfig;
import com.zenith.zenithrpc.constant.RpcConstant;
import com.zenith.zenithrpc.model.ServiceMetaInfo;
import com.zenith.zenithrpc.registry.Registry;
import com.zenith.zenithrpc.registry.RegistryFactory;
import com.zenith.zenithrpc.serializer.JdkSerializer;
import com.zenith.zenithrpc.serializer.Serializer;
import com.zenith.zenithrpc.model.RpcRequest;
import com.zenith.zenithrpc.model.RpcResponse;
import com.zenith.zenithrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.util.List;

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
            byte[] bodyBytes = serializer.serialize(rpcRequest);

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
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {

                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
