package com.zenith.zenithrpc.fault.tolerant;

import com.zenith.zenithrpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速容错实现
 *
 * @author zenith
 * @date 2024/09/06
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doResponse(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}
