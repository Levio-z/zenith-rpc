package com.zenith.zenithrpc.loadbalancer;

import com.zenith.zenithrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 *
 * @author zenith
 * @date 2024/08/31
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{
    /**
     * 一致性 Hash 环，存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();
    // 虚拟节点数
    private final int VIRTUAL_NODE_COUNT = 100;


    @Override
    public ServiceMetaInfo selectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        // 清空旧的虚拟节点
        virtualNodes.clear();

        for(ServiceMetaInfo serviceMetaInfo: serviceMetaInfoList){
            for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
                int hashCode = getHashCode(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hashCode, serviceMetaInfo);
            }
        }
        int hash = getHashCode(requestParams);

        // 选择最接近且大于调用请求hash的虚拟节点
        if (virtualNodes.ceilingKey(hash) != null){
            return virtualNodes.get(virtualNodes.ceilingKey(hash));
        }
        // 如果没有大于等于调用请求 hash 值的虚拟节点，则返回环首部的节点
        return virtualNodes.firstEntry().getValue();
    }

    /**
     * 哈希函数
     *
     * @param key
     * @return int
     */
    private int getHashCode(Object key){
        return key.hashCode();
    }
}
