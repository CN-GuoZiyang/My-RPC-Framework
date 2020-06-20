package top.guoziyang.test;

import top.guoziyang.rpc.transport.RpcClientProxy;
import top.guoziyang.rpc.api.HelloObject;
import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.serializer.KryoSerializer;
import top.guoziyang.rpc.transport.socket.client.SocketClient;

/**
 * 测试用消费者（客户端）
 *
 * @author ziyang
 */
public class SocketTestClient {

    public static void main(String[] args) {
<<<<<<< HEAD
        SocketClient client = new SocketClient("127.0.0.1", 9000);
=======
        SocketClient client = new SocketClient();
>>>>>>> 43f15ee ([v3.0] 基于Nacos实现了服务注册与发现)
        client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }

}
