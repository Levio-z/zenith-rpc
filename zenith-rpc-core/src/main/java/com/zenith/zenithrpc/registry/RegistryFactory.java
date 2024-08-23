package com.zenith.zenithrpc.registry;


import com.zenith.zenithrpc.serializer.SpiLoader;


/**
 * 注册中心工厂（用于获取注册中心对象）
 *
 * @author zenith
 * @date 2024/08/22
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }

}
