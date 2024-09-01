package com.zenith.zenithrpc.loadbalancer;

import com.zenith.zenithrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡器
 *
 * @author zenith
 * @date 2024/08/31
 */
public class RandomLoadBalancer implements LoadBalancer{

    @Override
    public ServiceMetaInfo selectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int index = Math.abs(ThreadLocalRandom.current().nextInt()) % serviceMetaInfoList.size();
        return serviceMetaInfoList.get(index);
    }
}
