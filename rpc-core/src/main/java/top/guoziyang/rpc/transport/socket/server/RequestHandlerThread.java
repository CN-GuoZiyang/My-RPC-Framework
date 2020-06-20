package top.guoziyang.rpc.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoziyang.rpc.handler.RequestHandler;
import top.guoziyang.rpc.entity.RpcRequest;
import top.guoziyang.rpc.entity.RpcResponse;
import top.guoziyang.rpc.registry.ServiceRegistry;
import top.guoziyang.rpc.serializer.CommonSerializer;
import top.guoziyang.rpc.transport.socket.util.ObjectReader;
import top.guoziyang.rpc.transport.socket.util.ObjectWriter;

import java.io.*;
import java.net.Socket;

/**
 * 处理RpcRequest的工作线程
 *
 * @author ziyang
 */
public class RequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            String interfaceName = rpcRequest.getInterfaceName();
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer);
        } catch (IOException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

}
