package top.guoziyang.rpc.transport;

import top.guoziyang.rpc.entity.RpcRequest;
import top.guoziyang.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 *
 * @author ziyang
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);

}
