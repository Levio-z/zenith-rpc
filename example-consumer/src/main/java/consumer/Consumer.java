package consumer;

import com.zenith.example.common.model.User;
import com.zenith.example.common.service.UserService;
import com.zenith.zenithrpc.RpcApplication;
import com.zenith.zenithrpc.proxy.ServiceProxyFactory;
import lombok.Data;

/**
 * 简单服务消费者示例
 */
@Data
public class Consumer {
    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();
        // 需要获取UserService实现类对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("name");

        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("打印用户名称："+newUser.getName());
        } else {
            System.out.println("user==null");
        }
        newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("打印用户名称："+newUser.getName());
        } else {
            System.out.println("user==null");
        }

        long number = userService.getNumber();
        System.out.println(number);
    }

}
