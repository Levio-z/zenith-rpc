package com.zenith.zenithrpc.serializer;

import java.util.HashMap;
import java.util.Map;


/**
 * 序列化器工厂（用于获取序列化器对象）
 *
 * @author zenith
 * @date 2024/08/16
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();


    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
