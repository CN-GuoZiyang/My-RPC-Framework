package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.registry.DefaultServiceRegistry;
import top.guoziyang.rpc.registry.ServiceRegistry;
import top.guoziyang.rpc.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 * @author ziyang
 */
public class SocketTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);
    }

}
