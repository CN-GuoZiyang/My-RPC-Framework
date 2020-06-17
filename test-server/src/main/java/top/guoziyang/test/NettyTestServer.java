package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.netty.server.NettyServer;
import top.guoziyang.rpc.registry.DefaultServiceRegistry;
import top.guoziyang.rpc.registry.ServiceRegistry;

/**
 * 测试用Netty服务提供者（服务端）
 * @author ziyang
 */
public class NettyTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }

}
