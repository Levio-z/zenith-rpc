package com.zenith.zenithrpc.protocol;

import lombok.Getter;

/**
 * 协议消息的状态枚举
 *
 * @author zenith
 * @date 2024/08/24
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok",20 ),
    BAD_REQUEST("badRequest", 40),
    BAD_RESPONSE("badResponse", 50);
    private final String text;
    private final int code;

    ProtocolMessageStatusEnum(String name, int code) {
        this.text = name;
        this.code = code;
    }

    /**
     * 根据value获取枚举
     *
     * @param code
     * @return {@link ProtocolMessageStatusEnum}
     */
    public static ProtocolMessageStatusEnum getEnumByCode(int code) {
        for (ProtocolMessageStatusEnum statusEnum : values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum;
            }
        }
        return null;
    }
}
