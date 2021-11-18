package top.guoziyang.rpc.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoziyang.rpc.transport.RpcServer;
import top.guoziyang.rpc.codec.CommonDecoder;
import top.guoziyang.rpc.codec.CommonEncoder;
import top.guoziyang.rpc.enumeration.RpcError;
import top.guoziyang.rpc.exception.RpcException;
import top.guoziyang.rpc.provider.ServiceProvider;
import top.guoziyang.rpc.provider.ServiceProviderImpl;
import top.guoziyang.rpc.registry.NacosServiceRegistry;
import top.guoziyang.rpc.registry.ServiceRegistry;
import top.guoziyang.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;

/**
 * NIO方式服务提供侧
 *
 * @author ziyang
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;
    // 本地保存服务的类是serviceProvider,serviceRegistry用来向远端nacos注册服务
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    private CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        // 一个服务端只能注册一个服务,这里有待改进
        start();
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 为boss线程添加日志处理
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 启用心跳，双方TCP套接字建立连接后（即都进入ESTABLISHED状态），
                    // 并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活，TCP会自动发送一个活动探测数据报文
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // TCP_NODELAY就是控制是否启用Nagle算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 根据编码继承的类，在输出的时候才会编码，同理，在输入的时候只会解码
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer)); //继承来自ChannelOutboundHandlerAdapter
                            pipeline.addLast(new CommonDecoder()); //继承自 inbound
                            pipeline.addLast(new NettyServerHandler()); //继承来自 inbound
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
