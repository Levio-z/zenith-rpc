package com.zenith.zenith.rpc.springboot.starter.bootstrap;

import com.zenith.zenith.rpc.springboot.starter.annotation.RpcService;
import com.zenith.zenithrpc.RpcApplication;
import com.zenith.zenithrpc.config.RegistryConfig;
import com.zenith.zenithrpc.config.RpcConfig;
import com.zenith.zenithrpc.model.ServiceMetaInfo;
import com.zenith.zenithrpc.registry.LocalRegistry;
import com.zenith.zenithrpc.registry.Registry;
import com.zenith.zenithrpc.registry.RegistryFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class RpcProviderBootstrap implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            Class<?> interfaceClass = rpcService.interfaceClass();

            // 默认值
            if(interfaceClass == void.class){
                // 需要注册服务
                interfaceClass = beanClass.getInterfaces()[0];
            }

            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            LocalRegistry.register(serviceName,beanClass);
            
            // 全局配置
            
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry  = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = ServiceMetaInfo.builder()
                    .serviceName(serviceName)
                    .serviceVersion(serviceVersion)
                    .serviceHost(rpcConfig.getHost()).servicePort(rpcConfig.getServerPort()).build();
            try {
                registry.register(serviceMetaInfo);
            }catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }

        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
