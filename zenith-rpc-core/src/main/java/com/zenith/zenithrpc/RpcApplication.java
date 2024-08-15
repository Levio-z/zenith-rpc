package com.zenith.zenithrpc;

import com.zenith.zenithrpc.config.RpcConfig;
import com.zenith.zenithrpc.constant.RpcConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
    }

    /**
     * 框架初始化，支持传入自定义配置
     *
     */
    public static void init() {
        RpcConfig rpcConfig;
        try{
            rpcConfig = com.zenith.zenithrpc.utils.ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            log.error("rpc init error", e);
            rpcConfig = new RpcConfig();
        }
        init(rpcConfig);
        }




    /**
     *  Get RpcConfig
     *
     * @return {@link RpcConfig}
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    rpcConfig = new RpcConfig();
                }
            }
        }
        return rpcConfig;
    }
}
