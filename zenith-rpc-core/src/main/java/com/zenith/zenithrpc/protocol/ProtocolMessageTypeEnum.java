package com.zenith.zenithrpc.protocol;

import lombok.Getter;

/**
 * 协议消息的类型枚举
 *
 * @author zenith
 * @date 2024/08/24
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST( 0),
    RESPONSE( 1),
    HEARTBEAT(2),
    OTHER(3);


    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举
     *
     * @param key
     * @return {@link ProtocolMessageTypeEnum}
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum typeEnum : values()) {
            if (typeEnum.key == key) {
                return typeEnum;
            }
        }
        return OTHER;
    }

}
