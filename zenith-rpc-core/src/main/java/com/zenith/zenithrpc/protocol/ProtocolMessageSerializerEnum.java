package com.zenith.zenithrpc.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息的序列化器枚举
 *
 * @author zenith
 * @date 2024/08/24
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0,"jdk"),
    JSON(1,"json"),
    KRYO(2,"kryo"),
    HESSIAN(3,"hessian");

    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum( int key,String value) {
        this.value = value;
        this.key = key;
    }

    /**
     * 根据key获取枚举
     *
     * @param key key
     * @return {@link ProtocolMessageSerializerEnum}
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key) {
        for (ProtocolMessageSerializerEnum anEnum : values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据key获取枚举
     *
     * @param value value
     * @return {@link ProtocolMessageSerializerEnum}
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value) {
        for (ProtocolMessageSerializerEnum anEnum : values()) {
            if (anEnum.value.equals(value) ) {
                return anEnum;
            }
        }
        return null;
    }


    /**
     * 获取值列表
     *
     * @return {@link List}<{@link String}>
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

}
