package consumer;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zenith.example.common.model.User;
import com.zenith.example.common.service.UserService;
import com.zenith.zenithrpc.serializer.JdkSerializer;
import com.zenith.zenithrpc.serializer.Serializer;
import com.zenith.zenithrpc.model.RpcRequest;
import com.zenith.zenithrpc.model.RpcResponse;

import java.io.IOException;

/**
 * 静态代理
 *
 * @author zenith
 * @date 2024/07/26
 */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
