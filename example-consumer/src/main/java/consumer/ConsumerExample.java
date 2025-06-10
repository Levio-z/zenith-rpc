package consumer;

import com.zenith.zenithrpc.config.RpcConfig;
import com.zenith.zenithrpc.utils.ConfigUtils;

/**
 * 测试读取配置
 *
 * @author zenith
 * @date 2024/08/15
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}

