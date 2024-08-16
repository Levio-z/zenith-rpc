package com.zenith.zenithrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zenith.zenithrpc.RpcApplication;
import com.zenith.zenithrpc.serializer.JdkSerializer;
import com.zenith.zenithrpc.serializer.Serializer;
import com.zenith.zenithrpc.model.RpcRequest;
import com.zenith.zenithrpc.model.RpcResponse;
import com.zenith.zenithrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

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

        // 发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8083")
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
