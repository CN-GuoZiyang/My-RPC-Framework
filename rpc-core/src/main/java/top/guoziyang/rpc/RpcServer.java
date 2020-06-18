package top.guoziyang.rpc;

import top.guoziyang.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 *
 * @author ziyang
 */
public interface RpcServer {

    void start(int port);

    void setSerializer(CommonSerializer serializer);

}
