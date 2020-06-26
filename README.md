# My-RPC-Framework

[![Build Status](https://travis-ci.com/CN-GuoZiyang/My-RPC-Framework.svg?branch=master)](https://travis-ci.com/CN-GuoZiyang/My-RPC-Framework)
![GitHub](https://img.shields.io/github/license/CN-GuoZiyang/My-RPC-Framework)
![jdk](https://img.shields.io/static/v1?label=oraclejdk&message=8&color=blue)

My-RPC-Framework 是一款基于 Nacos 实现的 RPC 框架。网络传输实现了基于 Java 原生 Socket 与 Netty 版本，并且实现了多种序列化与负载均衡算法。

## 架构

![系统架构](./images/architecture.png)

消费者调用提供者的方式取决于消费者的客户端选择，如选用原生 Socket 则该步调用使用 BIO，如选用 Netty 方式则该步调用使用 NIO。如该调用有返回值，则提供者向消费者发送返回值的方式同理。

## 特性

- 实现了基于 Java 原生 Socket 传输与 Netty 传输两种网络传输方式
- 实现了四种序列化算法，Json 方式、Kryo 算法、Hessian 算法与 Google Protobuf 方式（默认采用 Kryo方式序列化）
- 实现了两种负载均衡算法：随机算法与轮转算法
- 使用 Nacos 作为注册中心，管理服务提供者信息
- 消费端如采用 Netty 方式，会复用 Channel 避免多次连接
- 如消费端和提供者都采用 Netty 方式，会采用 Netty 的心跳机制，保证连接
- 接口抽象良好，模块耦合度低，网络传输、序列化器、负载均衡算法可配置
- 实现自定义的通信协议
- 服务提供侧自动注册服务

## 项目模块概览

- **roc-api**	——	通用接口
- **rpc-common**	——	实体对象、工具类等公用类
- **rpc-core**	——	框架的核心实现
- **test-client**	——	测试用消费侧
- **test-server**	——	测试用提供侧

## 传输协议（MRF协议）

调用参数与返回值的传输采用了如下 MRF 协议（ My-RPC-Framework 首字母）以防止粘包：

```
+---------------+---------------+-----------------+-------------+
|  Magic Number |  Package Type | Serializer Type | Data Length |
|    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
+---------------+---------------+-----------------+-------------+
|                          Data Bytes                           |
|                   Length: ${Data Length}                      |
+---------------------------------------------------------------+
```

| 字段            | 解释                                                         |
| :-------------- | :----------------------------------------------------------- |
| Magic Number    | 魔数，表识一个 MRF 协议包，0xCAFEBABE                        |
| Package Type    | 包类型，标明这是一个调用请求还是调用响应                     |
| Serializer Type | 序列化器类型，标明这个包的数据的序列化方式                   |
| Data Length     | 数据字节的长度                                               |
| Data Bytes      | 传输的对象，通常是一个`RpcRequest`或`RpcClient`对象，取决于`Package Type`字段，对象的序列化方式取决于`Serializer Type`字段。 |

## 使用

### 定义调用接口

```java
package top.guoziyang.rpc.api;

public interface HelloService {
    String hello(String name);
}
```

### 在服务提供侧实现该接口

```java
package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
```

### 编写服务提供者

```java
package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.netty.server.NettyServer;

@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }
}
```

这里选用 Netty 传输方式，并且指定序列化方式为 Google Protobuf 方式。

### 在服务消费侧远程调用

```java
package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.RpcClient;
import top.guoziyang.rpc.transport.RpcClientProxy;
import top.guoziyang.rpc.transport.netty.client.NettyClient;

public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER, new RoundRobinLoadBalancer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String res = helloService.hello("ziyang");
        System.out.println(res);
    }
}
```

这里客户端也选用了 Netty 的传输方式，序列化方式采用 Kryo 方式，负载均衡策略指定为轮转方式。

### 启动

在此之前请确保 Nacos 运行在本地 `8848` 端口。

首先启动服务提供者，再启动消费者，在消费侧会输出`Hello, ziyang`。

## TODO

- 配置文件

## LICENSE

My-RPC-Framework is under the MIT license. See the [LICENSE](https://github.com/CN-GuoZiyang/My-RPC-Framework/blob/master/LICENSE) file for details.
