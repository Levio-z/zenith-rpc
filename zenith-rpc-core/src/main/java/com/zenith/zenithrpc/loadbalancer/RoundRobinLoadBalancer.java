package com.zenith.zenithrpc.loadbalancer;

import com.zenith.zenithrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 *
 * @author zenith
 * @date 2024/08/31
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
    // 当前轮询的下标 原子计数器 AtomicInteger

    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo selectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        // 取模算法轮询
        int index = count.getAndIncrement() % serviceMetaInfoList.size();

        return serviceMetaInfoList.get(index);
    }
}
