package top.guoziyang.test;

import top.guoziyang.rpc.annotation.ServiceScan;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.RpcServer;
import top.guoziyang.rpc.transport.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 *
 * @author ziyang
 */
@ServiceScan
public class SocketTestServer {

    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        server.start();
    }

}
