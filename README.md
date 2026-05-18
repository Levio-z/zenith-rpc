# Zenith-RPC 整体架构设计

## 一、架构总览

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              Zenith-RPC 架构                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                           应用层                                      │   │
│  │  ┌─────────────────┐                     ┌─────────────────┐         │   │
│  │  │  服务提供者      │                     │  服务消费者      │         │   │
│  │  │ (Provider)      │                     │ (Consumer)      │         │   │
│  │  └────────┬────────┘                     └────────┬────────┘         │   │
│  │           │                                       │                   │   │
│  └───────────┼───────────────────────────────────────┼───────────────────┘   │
│              │                                       │                       │
│  ┌───────────┼───────────────────────────────────────┼───────────────────┐   │
│  │           │            Bootstrap 层               │                   │   │
│  │  ┌────────┴────────┐                     ┌────────┴────────┐          │   │
│  │  │ ProviderBootstrap│                    │ ConsumerBootstrap│          │   │
│  │  └────────┬────────┘                     └────────┬────────┘          │   │
│  │           │                                       │                   │   │
│  └───────────┼───────────────────────────────────────┼───────────────────┘   │
│              │                                       │                       │
│  ┌───────────┼───────────────────────────────────────┼───────────────────┐   │
│  │           │           核心框架层                   │                   │   │
│  │           │                                       │                   │   │
│  │           ▼                                       ▼                   │   │
│  │  ┌─────────────────┐                     ┌─────────────────┐          │   │
│  │  │ 本地注册表注册/  │                     │    代理工厂      │          │   │
│  │  │  注册到注册中心  │                     │ ServiceProxyFactory│        │   │
│  │  └─────────────────┘                     └─────────────────┘          │   │
│  │           │                                       │                   │   │
│  │           ▼                                       │                   │   │
│  │  ┌─────────────────────────────────────────────────────┐             │   │
│  │  │                      RpcApplication                 │             │   │
│  │  │                   (框架核心入口)                     │             │   │
│  │  └─────────────────────────────────────────────────────┘             │   │
│  │                                                                     │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                          协议层                                        │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐             │   │
│  │  │ Protocol │  │ Encoder  │  │ Decoder  │  │ TcpServer│             │   │
│  │  │ Message  │  │          │  │          │  │ Handler  │             │   │
│  │  └──────────┘  └──────────┘  └──────────┘  └──────────┘             │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                         可扩展组件层                                  │   │
│  │                                                                       │   │
│  │  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐         │   │
│  │  │   序列化器     │  │   负载均衡     │  │   重试策略      │         │   │
│  │  │  Serializer   │  │  LoadBalancer  │  │  RetryStrategy  │         │   │
│  │  └────────────────┘  └────────────────┘  └────────────────┘         │   │
│  │                                                                       │   │
│  │  ┌────────────────┐  ┌────────────────┐                              │   │
│  │  │   容错策略     │  │   注册中心     │                              │   │
│  │  │ TolerantStrategy│ │    Registry    │                              │   │
│  │  └────────────────┘  └────────────────┘                              │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 二、模块说明

### 2.1 项目结构

```
zenith-rpc/
├── zenith-rpc-core/              # 核心模块
│   └── src/main/java/
│       └── com/zenith/zenithrpc/
│           ├── RpcApplication.java       # 框架入口
│           ├── bootstrap/                # 启动模块
│           │   ├── ProviderBootstrap.java
│           │   └── ConsumerBootstrap.java
│           ├── config/                   # 配置模块
│           │   ├── RpcConfig.java
│           │   └── RegistryConfig.java
│           ├── constant/                 # 常量
│           │   └── RpcConstant.java
│           ├── model/                    # 数据模型
│           │   ├── RpcRequest.java
│           │   ├── RpcResponse.java
│           │   ├── ServiceMetaInfo.java
│           │   └── ServiceRegisterInfo.java
│           ├── protocol/                 # 协议
│           │   ├── ProtocolMessage.java
│           │   ├── ProtocolConstant.java
│           │   └── ProtocolMessageTypeEnum.java
│           ├── registry/                 # 注册中心
│           │   ├── Registry.java
│           │   ├── LocalRegistry.java
│           │   └── EtcdRegistry.java
│           ├── serializer/               # 序列化
│           │   ├── Serializer.java
│           │   ├── JdkSerializer.java
│           │   └── SpiLoader.java
│           ├── proxy/                    # 代理
│           │   ├── ServiceProxy.java
│           │   └── ServiceProxyFactory.java
│           ├── loadbalancer/             # 负载均衡
│           │   ├── LoadBalancer.java
│           │   ├── RandomLoadBalancer.java
│           │   └── RoundRobinLoadBalancer.java
│           ├── fault/                    # 容错
│           │   ├── retry/               # 重试
│           │   │   ├── RetryStrategy.java
│           │   │   └── FixedIntervalRetryStrategy.java
│           │   └── tolerant/            # 容错
│           │       ├── TolerantStrategy.java
│           │       └── FailFastTolerantStrategy.java
│           ├── server/                   # 服务器
│           │   ├── HttpServer.java
│           │   └── tcp/
│           │       ├── VertxTcpServer.java
│           │       ├── VertxTcpClient.java
│           │       ├── ProtocolMessageEncoder.java
│           │       ├── ProtocolMessageDecoder.java
│           │       └── TcpServerHandler.java
│           └── utils/                    # 工具
│               └── ConfigUtils.java
│
├── zenith-rpc-spring-boot-starter/  # Spring Boot 集成
├── example-provider/                # 服务提供者示例
├── example-consumer/                # 服务消费者示例
└── example-common/                  # 公共模块
```

### 2.2 核心模块职责

| 模块         | 职责         | 关键类                                               |
| ------------ | ------------ | ---------------------------------------------------- |
| bootstrap    | 统一启动入口 | RpcApplication, ProviderBootstrap, ConsumerBootstrap |
| config       | 配置管理     | RpcConfig, RegistryConfig                            |
| model        | 数据模型     | RpcRequest, RpcResponse, ServiceMetaInfo             |
| protocol     | 协议定义     | ProtocolMessage, ProtocolConstant                    |
| registry     | 服务注册发现 | Registry, LocalRegistry, EtcdRegistry                |
| serializer   | 序列化       | Serializer, JdkSerializer, KryoSerializer            |
| proxy        | 动态代理     | ServiceProxy, ServiceProxyFactory                    |
| loadbalancer | 负载均衡     | LoadBalancer, RandomLoadBalancer                     |
| fault        | 容错重试     | RetryStrategy, TolerantStrategy                      |
| server       | 网络通信     | VertxTcpServer, TcpServerHandler                     |

---

## 三、核心组件

### 3.1 RpcApplication

框架核心入口类，负责：

- 配置加载和初始化
- 注册中心初始化
- 生命周期管理

```java
public class RpcApplication {
    public static void init();
    public static void init(RpcConfig config);
    public static RpcConfig getRpcConfig();
}
```

### 3.2 Bootstrap 组件

**ProviderBootstrap**：服务提供者启动

- 注册本地服务
- 注册到注册中心
- 启动 TCP 服务器

**ConsumerBootstrap**：服务消费者启动

- 初始化框架
- 创建代理

### 3.3 协议组件

```
┌─────────────────────────────────────────────────────┐
│               协议消息格式                            │
├─────────────────────────────────────────────────────┤
│                                                     │
│  Header (17 bytes)  │  Body (N bytes)              │
│  ┌────┬────┬────┐   │  ┌─────────────────────┐     │
│  │magic│ver │ser │   │  │                     │     │
│  ├────┴────┼────┤   │  │   序列化后的消息体   │     │
│  │ type│status│   │  │                     │     │
│  ├─────────┴────┤   │  │                     │     │
│  │  requestId   │   │  │                     │     │
│  ├──────────────┤   │  │                     │     │
│  │  bodyLength  │   │  └─────────────────────┘     │
│  └──────────────┘   │                               │
│                                                     │
└─────────────────────────────────────────────────────┘
```

### 3.4 可扩展组件

所有可扩展组件都采用 **SPI + 工厂模式** 实现：

```
┌────────────────────────────────────────────────────────┐
│                    SPI 扩展机制                         │
├────────────────────────────────────────────────────────┤
│                                                        │
│  ┌──────────────────────────────────────────────────┐  │
│  │         META-INF/rpc/system/                     │  │
│  │  ┌────────────────────────────────────────┐     │  │
│  │  │  com.zenith.zenithrpc.serializer.Serializer│   │  │
│  │  ├────────────────────────────────────────┤     │  │
│  │  │  jdk=com.zenith.zenithrpc...JdkSerializer │   │  │
│  │  │  json=com.zenith.zenithrpc...JsonSerializer│  │  │
│  │  │  kryo=com.zenith.zenithrpc...KryoSerializer│  │  │
│  │  └────────────────────────────────────────┘     │  │
│  └──────────────────────────────────────────────────┘  │
│                                                        │
│                        ▼                               │
│  ┌──────────────────────────────────────────────────┐  │
│  │                    SpiLoader                     │  │
│  │              (动态加载实现类)                      │  │
│  └──────────────────────────────────────────────────┘  │
│                                                        │
│                        ▼                               │
│  ┌──────────────────────────────────────────────────┐  │
│  │                  Factory                          │  │
│  │         SerializerFactory.getInstance(key)        │  │
│  └──────────────────────────────────────────────────┘  │
│                                                        │
└────────────────────────────────────────────────────────┘
```

---

## 四、调用流程

### 4.1 服务提供者启动流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    Provider 启动流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ProviderBootstrap.init(serviceList)                            │
│           │                                                      │
│           ▼                                                      │
│  ┌─────────────────────┐                                        │
│  │  RpcApplication.init()│                                       │
│  │  ├─ 加载配置          │                                       │
│  │  ├─ 初始化注册中心    │                                       │
│  │  └─ 注册ShutdownHook │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────┐                                        │
│  │ LocalRegistry.register()│                                     │
│  │  服务名 → 实现类       │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────┐                                        │
│  │   Registry.register() │                                      │
│  │  注册服务到注册中心    │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────┐                                        │
│  │ VertxTcpServer.doStart()│                                    │
│  │  启动TCP服务器监听端口 │                                       │
│  └─────────────────────┘                                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 服务消费者调用流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    Consumer 调用流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ConsumerBootstrap.init()                                       │
│           │                                                      │
│           ▼                                                      │
│  ┌─────────────────────┐                                        │
│  │ RpcApplication.init() │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────────────────────┐                       │
│  │ ServiceProxyFactory.getProxy(serviceClass) │                  │
│  │  返回JDK动态代理对象                   │                      │
│  └──────────┬──────────────────────────┘                       │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────────────────────┐                       │
│  │ 代理对象.method(args)                │                       │
│  │  ↓ 调用InvocationHandler.invoke()   │                       │
│  └──────────┬──────────────────────────┘                       │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────────────────────┐                       │
│  │ 1. 构造RpcRequest                     │                       │
│  │ 2. 获取服务列表(Registry.discovery)    │                       │
│  │ 3. 负载均衡选择服务                   │                       │
│  │ 4. 重试策略执行调用                   │                       │
│  │ 5. VertxTcpClient.doRequest()        │                       │
│  └──────────┬──────────────────────────┘                       │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────────────────────┐                       │
│  │     TCP通信 + 协议编解码              │                       │
│  └──────────┬──────────────────────────┘                       │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────────────────────┐                       │
│  │     返回RpcResponse                   │                       │
│  │     返回调用结果                       │                       │
│  └─────────────────────────────────────┘                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.3 服务端请求处理流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    Server 请求处理流程                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  NetSocket 收到数据                                              │
│           │                                                      │
│           ▼                                                      │
│  ┌─────────────────────┐                                        │
│  │ TcpBufferHandlerWrapper│                                     │
│  │  处理粘包半包问题     │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────┐                                        │
│  │ ProtocolMessageDecoder│                                     │
│  │  解析协议头和消息体    │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────┐                                        │
│  │    TcpServerHandler  │                                       │
│  │  ├─ 获取服务类        │                                       │
│  │  ├─ 反射调用方法      │                                       │
│  │  └─ 构造响应结果      │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────┐                                        │
│  │ ProtocolMessageEncoder│                                     │
│  │  编码响应消息        │                                       │
│  └──────────┬──────────┘                                        │
│             │                                                    │
│             ▼                                                    │
│  ┌─────────────────────┐                                        │
│  │    NetSocket.write() │                                      │
│  │    发送响应给客户端   │                                       │
│  └─────────────────────┘                                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 五、扩展点说明

### 5.1 序列化器扩展

| 键名    | 实现类            | 说明           |
| ------- | ----------------- | -------------- |
| jdk     | JdkSerializer     | JDK原生序列化  |
| json    | JsonSerializer    | JSON序列化     |
| kryo    | KryoSerializer    | Kryo高效序列化 |
| hessian | HessianSerializer | Hessian序列化  |

### 5.2 注册中心扩展

| 键名 | 实现类            | 说明              |
| ---- | ----------------- | ----------------- |
| etcd | EtcdRegistry      | Etcd注册中心      |
| zk   | ZooKeeperRegistry | Zookeeper注册中心 |

### 5.3 负载均衡扩展

| 键名           | 实现类                     | 说明         |
| -------------- | -------------------------- | ------------ |
| random         | RandomLoadBalancer         | 随机负载均衡 |
| roundRobin     | RoundRobinLoadBalancer     | 轮询负载均衡 |
| consistentHash | ConsistentHashLoadBalancer | 一致性哈希   |

### 5.4 重试策略扩展

| 键名          | 实现类                     | 说明         |
| ------------- | -------------------------- | ------------ |
| no            | NoRetryStrategy            | 不重试       |
| fixedInterval | FixedIntervalRetryStrategy | 固定间隔重试 |

### 5.5 容错策略扩展

| 键名     | 实现类                   | 说明     |
| -------- | ------------------------ | -------- |
| failFast | FailFastTolerantStrategy | 快速失败 |
| failSafe | FailSafeTolerantStrategy | 安全失败 |

---

## 六、设计模式应用

### 6.1 工厂模式

应用在：SerializerFactory, LoadBalancerFactory, RetryStrategyFactory, TolerantStrategyFactory

```java
public class SerializerFactory {
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
```

### 6.2 装饰者模式

应用在：TcpBufferHandlerWrapper

```java
// 装饰 RecordParser，添加粘包半包处理能力
public class TcpBufferHandlerWrapper implements Handler<Buffer> {
    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }
}
```

### 6.3 代理模式

应用在：ServiceProxy

```java
// JDK动态代理InvocationHandler
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 构造请求、发送调用、返回结果
    }
}
```

### 6.4 SPI模式

所有可扩展组件都通过SPI机制加载：

```java
// SpiLoader负责动态加载实现类
public class SpiLoader {
    public static <T> T getInstance(Class<?> tClass, String key) {
        // 从配置文件加载并返回实例
    }
}
```

---

## 七、配置说明

### 7.1 全局配置

```yaml
rpc:
  name: zenith-rpc
  version: 1.0
  host: localhost
  serverPort: 8082
  mock: false
  serializer: jdk
  loadBalancer: roundRobin
  retryStrategy: no
  tolerantStrategy: failFast
  registryConfig:
    registry: etcd
    address: http://localhost:2380
    timeout: 10000
```

### 7.2 配置优先级

```
命令行参数 > 代码配置 > 配置文件 > 默认值
```

---

## 八、与主流RPC框架对比

| 特性       | Zenith-RPC | Dubbo    | gRPC     |
| ---------- | ---------- | -------- | -------- |
| 通信协议   | 自定义TCP  | 多协议   | HTTP/2   |
| 序列化     | 多协议     | 多协议   | Protobuf |
| 注册中心   | Etcd/ZK    | 多协议   | 无内置   |
| 负载均衡   | 多种算法   | 多种算法 | 简单轮询 |
| 重试机制   | 支持       | 支持     | 支持     |
| Spring集成 | Starter    | 完善     | 支持     |
| 成熟度     | 学习中     | 生产级   | 生产级   |

---

## 九、总结

Zenith-RPC 框架的设计特点：

1. **模块化设计**：各组件职责清晰，易于扩展
2. **SPI机制**：支持动态加载，方便自定义
3. **设计模式**：工厂、装饰者、代理等模式灵活应用
4. **可配置**：通过配置文件和SPI支持灵活定制
5. **学习导向**：参考Dubbo设计，适合学习RPC原理
