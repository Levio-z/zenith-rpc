package com.zenith.zenithrpc.fault.retry;

import com.github.rholder.retry.*;
import com.zenith.zenithrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 固定时间间隔 - 重试策略
 *
 * @author zenith
 * @date 2024/09/01
 */
public class FixedIntervalRetryStrategy implements RetryStrategy{
    /**
     * 固定时间间隔 - 重试策略
     *
     * @param callable
     * @return {@link RpcResponse}
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        // Guava-Retrying 提供的 RetryerBuilder
        return RetryerBuilder.<RpcResponse>newBuilder()
                // 设置重试条件，这里是重试所有异常
                .retryIfExceptionOfType(Exception.class)
                // 设置重试等待策略，这里是每次重试等待 1 秒
                .withWaitStrategy(WaitStrategies.fixedWait(3L, java.util.concurrent.TimeUnit.SECONDS))
                // 设置重试停止策略，这里是重试 3 次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener( new RetryListener(){
                     @Override
                     public <V> void onRetry(Attempt<V> attempt) {
                         if (attempt.getAttemptNumber()>1){
                             System.out.println("重试次数：" +(attempt.getAttemptNumber()-1));
                         }
                     }
                }).build().call(callable);

    }
}
