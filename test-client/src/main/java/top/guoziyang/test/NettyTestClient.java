package top.guoziyang.test;

import top.guoziyang.rpc.api.ByeService;
import top.guoziyang.rpc.api.HelloObject;
import top.guoziyang.rpc.api.HelloService;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.RpcClient;
import top.guoziyang.rpc.transport.RpcClientProxy;
import top.guoziyang.rpc.transport.netty.client.NettyClient;

/**
 * 测试用Netty消费者
 *
 * @author ziyang
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }

}
