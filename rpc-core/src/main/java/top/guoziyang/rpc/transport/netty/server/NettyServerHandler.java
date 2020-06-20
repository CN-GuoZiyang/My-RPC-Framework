package top.guoziyang.rpc.transport.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoziyang.rpc.factory.SingletonFactory;
import top.guoziyang.rpc.handler.RequestHandler;
import top.guoziyang.rpc.entity.RpcRequest;
import top.guoziyang.rpc.entity.RpcResponse;
import top.guoziyang.rpc.factory.ThreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * Netty中处理RpcRequest的Handler
 *
 * @author ziyang
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private final ExecutorService threadPool;
    private final RequestHandler requestHandler;

    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
        this.threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        // 引入线程池来处理业务,防止netty的worker线程长时间阻塞,导致不能处理read事件
        threadPool.execute(() -> {
            try {
                logger.info("服务器接收到请求: {}", msg);
                Object result = requestHandler.handle(msg);
                ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
                future.addListener(ChannelFutureListener.CLOSE);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}
