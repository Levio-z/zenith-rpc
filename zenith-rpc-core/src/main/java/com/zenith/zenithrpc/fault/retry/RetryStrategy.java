package com.zenith.zenithrpc.fault.retry;

import com.zenith.zenithrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 *
 * @author zenith
 * @date 2024/09/01
 */
public interface RetryStrategy {
    /**
     * 重试策略
     *
     * @param callable
     * @return {@link RpcResponse}
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
