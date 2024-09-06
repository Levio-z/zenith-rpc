package com.zenith.zenithrpc.fault.tolerant;

import com.zenith.zenithrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doResponse(Map<String, Object> context, Exception e) {
        log.info("静默处理异常", e);
        return new RpcResponse();
    }
}
