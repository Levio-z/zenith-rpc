package com.zenith.zenithrpc.fault.retry;

import com.zenith.zenithrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试 -充实策略
 *
 * @author zenith
 * @date 2024/09/01
 */
public class NoRetryStrategy implements RetryStrategy{
    /**
     * 不重试实现
     *
     * @param callable
     * @return {@link RpcResponse}
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
