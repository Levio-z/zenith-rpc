package com.zenith.zenith.rpc.springboot.starter.annotation;


import com.zenith.zenith.rpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import com.zenith.zenith.rpc.springboot.starter.bootstrap.RpcInitBootstrap;
import com.zenith.zenith.rpc.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {
    /**
     * 需要启动 server
     */
    boolean needServer() default true;
}
