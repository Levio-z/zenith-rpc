package com.zenith.zenithrpc.fault.tolerant;

import com.zenith.zenithrpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 *
 * @author zenith
 * @date 2024/09/06
 */
public interface TolerantStrategy {
    RpcResponse doResponse(Map<String, Object> context, Exception e);
}
