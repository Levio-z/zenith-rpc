package com.zenith.zenithrpc.loadbalancer;

import com.zenith.zenithrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（消费端使用）
 *
 * @author zenith
 * @date 2024/08/31
 */
public interface LoadBalancer {
    // 选择服务调用 根据请求参数和可用服务列表
    ServiceMetaInfo  selectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
     default ServiceMetaInfo defaultSelectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList){
         if (serviceMetaInfoList.isEmpty()){
             return null;
         }
         // 只有一个服务，无需轮询
         if (serviceMetaInfoList.size() == 1){
             System.out.println("无需轮询：");
             return serviceMetaInfoList.get(0);
         }
         return selectService(requestParams,serviceMetaInfoList);
     }
}
