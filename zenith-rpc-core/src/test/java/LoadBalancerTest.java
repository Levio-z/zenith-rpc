import com.zenith.zenithrpc.loadbalancer.ConsistentHashLoadBalancer;
import com.zenith.zenithrpc.loadbalancer.LoadBalancer;
import com.zenith.zenithrpc.loadbalancer.RandomLoadBalancer;
import com.zenith.zenithrpc.model.ServiceMetaInfo;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancerTest {
    final LoadBalancer loadBalancer = new RandomLoadBalancer();
    @Test
    public void testLoadBalancer(){
        // 请求参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", "apple");

        // 服务列表
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        // 三个不同的服务列表，放进list
        serviceMetaInfo1.setServiceName("apple");
        serviceMetaInfo1.setServiceHost("127.0.0.1");
        serviceMetaInfo1.setServicePort(8080);
        serviceMetaInfo1.setServiceVersion("1.0");
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
         serviceMetaInfo2.setServiceName("apple");
         serviceMetaInfo2.setServiceHost("127.0.0.1");
         serviceMetaInfo2.setServicePort(8081);
         serviceMetaInfo2.setServiceVersion("1.0");

          ServiceMetaInfo serviceMetaInfo3 = new ServiceMetaInfo();
          serviceMetaInfo3.setServiceName("apple");
           serviceMetaInfo3.setServiceHost("127.0.0.1");
           serviceMetaInfo3.setServicePort(8082);
           serviceMetaInfo3.setServiceVersion("1.0");
        List<ServiceMetaInfo> serviceMetaInfos = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2, serviceMetaInfo3);

        // 连续调用3次
        ServiceMetaInfo serviceMetaInfo = loadBalancer.selectService(requestParams, serviceMetaInfos);
        System.out.println(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.selectService(requestParams, serviceMetaInfos);
        System.out.println(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.selectService(requestParams, serviceMetaInfos);
        System.out.println(serviceMetaInfo);

    }
}
