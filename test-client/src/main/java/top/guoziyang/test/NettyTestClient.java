package top.guoziyang.test;

import top.guoziyang.rpc.RpcClient;
import top.guoziyang.rpc.RpcClientProxy;
import top.guoziyang.rpc.api.HelloObject;
import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.netty.client.NettyClient;
import top.guoziyang.rpc.serializer.HessianSerializer;
import top.guoziyang.rpc.serializer.ProtobufSerializer;

/**
 * 测试用Netty消费者
 *
 * @author ziyang
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        client.setSerializer(new ProtobufSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);

    }

}
