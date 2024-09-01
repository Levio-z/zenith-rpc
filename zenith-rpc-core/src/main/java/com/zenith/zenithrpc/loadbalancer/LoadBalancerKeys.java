package com.zenith.zenithrpc.loadbalancer;

/**
 * 负载均衡器常量
 *
 * @author zenith
 * @date 2024/08/31
 */
public interface LoadBalancerKeys {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     *随机
     */
    String RANDOM = "random";

    /**
     *一致性哈希
     */
    String CONSISTENT_HASH = "consistentHash";
}
