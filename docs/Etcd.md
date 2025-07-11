### Etcd 数据结构与特性
Etcd 使用键值对存储数据，支持嵌套目录结构。每个键值对都可以有一个版本号和过期时间。Etcd 的数据结构如下：
Etcd 的核心数据结构包括：
- 键（Key）：字符串，支持嵌套目录结构，例如 `/services/user/1.0.0`。
- 值（Value）：任意类型的数据，通常是字符串。
- 版本号：每个键都有一个与之关联的版本号，用于跟踪键的修改历史。当一个键的值发生变化时，其版本号也会增加。
特性：
- Lease 机制：Etcd 支持 Lease 机制，即数据可以设置过期时间，到期后数据会自动删除。
- Watch 机制：Etcd 支持 Watch 机制，即客户端可以监听某个键的变化，当键值对被修改时，会收到通知。
- 实现节点过期和监听：Etcd 使用 Lease 机制来实现节点的过期和监听。
  - 当节点过期时，Etcd 会将节点的数据删除
  - 当节点被监听时，Etcd 会将节点的数据发送给监听的 client。
- 保证数据一致性
  - 从表层来看，Etcd 支持事务操作，能够保证数据一致性。
  - 从底层来看‌，Etcd 使用 Raft 一致性算法来保证数据的一致性。
  - Playground 地址：http://play.etcd.io/play
Etcd 基本操作
  - 写入数据：使用 PUT 命令写入键值对。
  - 读取数据：使用 GET 命令读取键值对。
  - 根据前缀读取数据：使用 PREFIX 命令读取以某个前缀开头的键值对。
Etcd安装
  - 官方下载页：https://github.com/etcd-io/etcd/releases
  - https://etcd.io/docs/v3.2/install/
三个脚本
- etcd：etcd 服务本身
- etcdctl：客户端，用于操作 etcd，比如读写数据
- etcdutl：备份恢复工具
执行etcd脚本启动etcd
- 2379端口：http://127.0.0.1:2379 提供Http api服务，和卡户段交互
- 2380端口：http://127.0.0.1:2380 集群中节点通讯
Etcd 可视化工具
- etcdkeeper：️https://github.com/evildecay/etcdkeeper/

启动可视化界面
`./etcdkeeper -p 8081`

安装后访问，http://127.0.0.1:8081/etcdkeeper/
Etcd Java 客户端
 jetcd：https://github.com/etcd-io/jetcd。
 Java 版本必须大于 11！

 1）首先在项目中引入 jetcd：
 ```xml
 <!-- https://mvnrepository.com/artifact/io.etcd/jetcd-core -->
<dependency>
    <groupId>io.etcd</groupId>
    <artifactId>jetcd-core</artifactId>
    <version>0.7.7</version>
</dependency>
 ```
2）按照官方文档的示例写 Demo：
```java
package com.xxx;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EtcdRegistry {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // create client using endpoints
        Client client = Client.builder().endpoints("http://localhost:2379")
                .build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        // put the key-value
        kvClient.put(key, value).get();

        // get the CompletableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        // get the value from CompletableFuture
        GetResponse response = getFuture.get();

        // delete the key
        kvClient.delete(key).get();
    }
}

```
异步调用 get() 获取指定 key 对应的值，返回 CompletableFuture。

.get() 阻塞直到数据返回，获取 GetResponse。
常用的客户端和作用如下，仅作了解即可：
- kvClient：用于对 etcd 中的键值对进行操作。通过 kvClient 可以进行设置值、获取值、删除值、列出目录等操作。
- leaseClient：用于管理 etcd 的租约机制。租约是 etcd 中的一种时间片，用于为键值对分配生存时间，并在租约到期时自动删除相关的键值对。通过 leaseClient 可以创建、获取、续约和撤销租约。
- watchClient：用于监视 etcd 中键的变化，并在键的值发生变化时接收通知。
- clusterClient：用于与 etcd 集群进行交互，包括添加、移除、列出成员、设置选举、获取集群的健康状态、获取成员列表信息等操作。
- authClient：用于管理 etcd 的身份验证和授权。通过 authClient 可以添加、删除、列出用户、角色等身份信息，以及授予或撤销用户或角色的权限。
- maintenanceClient：用于执行 etcd 的维护操作，如健康检查、数据库备份、成员维护、数据库快照、数据库压缩等。
- lockClient：用于实现分布式锁功能，通过 lockClient 可以在 etcd 上创建、获取、释放锁，能够轻松实现并发控制。
- electionClient：用于实现分布式选举功能，可以在 etcd 上创建选举、提交选票、监视选举结果等。

通过使用 etcd 的 Watch API，可以监视键的变化，并在发生变化时接收通知。这种版本机制使得 etcd 在分布式系统中能够实现乐观并发控制、一致性和可靠性的数据访问。

