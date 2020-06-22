package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.netty.server.NettyServer;
import top.guoziyang.rpc.provider.ServiceProviderImpl;
import top.guoziyang.rpc.registry.ServiceRegistry;
import top.guoziyang.rpc.serializer.ProtobufSerializer;

/**
 * 测试用Netty服务提供者（服务端）
 *
 * @author ziyang
 */
public class NettyTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.publishService(helloService, HelloService.class);
    }

}
