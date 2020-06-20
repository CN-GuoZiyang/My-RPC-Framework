package top.guoziyang.test;

import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.provider.ServiceProviderImpl;
import top.guoziyang.rpc.registry.ServiceRegistry;
import top.guoziyang.rpc.serializer.HessianSerializer;
import top.guoziyang.rpc.transport.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 *
 * @author ziyang
 */
public class SocketTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.publishService(helloService, HelloService.class);
    }

}
