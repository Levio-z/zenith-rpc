package com.zenith.zenithrpc.loadbalancer;

import com.zenith.zenithrpc.serializer.SpiLoader;

/**
 * 负载均衡器工厂模式
 *
 * @author zenith
 * @date 2024/08/31
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }


    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取负载均衡器
     *
     * @param loadBalancerKey 负载均衡器key
     * @return {@link LoadBalancer}
     */
    public static LoadBalancer getLoadBalancer(String loadBalancerKey) {
        return SpiLoader.getInstance(LoadBalancer.class, loadBalancerKey);
    }

}
