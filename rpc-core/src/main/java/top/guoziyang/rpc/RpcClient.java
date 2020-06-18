package top.guoziyang.rpc;

import top.guoziyang.rpc.entity.RpcRequest;
import top.guoziyang.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 *
 * @author ziyang
 */
public interface RpcClient {

    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);

}
