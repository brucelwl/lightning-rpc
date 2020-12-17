package com.bruce.lightning.rpc.client;


import com.bruce.lightning.rpc.client.initial.marshalling.MarshallingClientHandlerChannelInitializer;
import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.common.RpcResponse;
import com.bruce.lightning.rpc.util.PlatformUtil;
import com.bruce.lightning.rpc.util.ReflectUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class LightningClient {
    private static final Logger log = LoggerFactory.getLogger(LightningClient.class);

    private static final ConcurrentHashMap<Class<?>, Object> PROXY_INSTANCES = new ConcurrentHashMap<>();

    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;
    private Bootstrap bootstrap;

    public LightningClient() {

        //String property = DefaultEnvironment.getInstance().getProperty("lightning.rpc.serial.type");

        DefaultThreadFactory workerThreadFactory = new DefaultThreadFactory("RpcClientWorker-");
        ReflectUtils.set(workerThreadFactory, DefaultThreadFactory.class, "prefix", "RpcClientWorker-");

        workerGroup = PlatformUtil.isLinux() ? new EpollEventLoopGroup(workerThreadFactory) : new NioEventLoopGroup(workerThreadFactory);
        Class<? extends SocketChannel> socketChannelClass = PlatformUtil.isLinux() ? EpollSocketChannel.class : NioSocketChannel.class;
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(socketChannelClass)
                //.option(, )
                .handler(new MarshallingClientHandlerChannelInitializer());
    }

    /**
     * 同步连接
     */
    public void startSync(String host, int port) {
        try {
            //同步等待连接,连接成功后继续执行
            channelFuture = bootstrap.connect(host, port).sync();
            log.info("Lightning rpc client started !!! connect to server:{}:{}", host, port);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SocketAddress getRemoteAddress() {
        SocketAddress socketAddress = channelFuture.channel().remoteAddress();
        return socketAddress;
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> rpcInterface, int timeout) {
        //接口
        return (T) PROXY_INSTANCES.computeIfAbsent(rpcInterface, (key) -> {
            RpcMethodInterceptor rpcMethodInterceptor = new RpcMethodInterceptor(rpcInterface, timeout, this);
            return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{rpcInterface}, rpcMethodInterceptor);
        });
    }

    /**
     * 断开与服务端的连接,释放资源
     */
    public void close() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (channelFuture != null) {
            channelFuture.channel().close();
        }
        log.info("destroy Lightning rpc client");
    }

    /**
     * 发送数据请求
     */
    public DefaultResponseFuture send(RpcRequest request) {
        ChannelFuture channelFuture = this.channelFuture.channel().writeAndFlush(request);

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Throwable cause = future.cause();
                if (cause != null) {
                    RpcResponse resp = new RpcResponse();
                    resp.setRequestId(request.getId());
                    resp.setCode("error");
                    resp.setThrowable(cause);
                    DefaultResponseFuture.receive(resp);
                }
            }
        });

        return new DefaultResponseFuture(request);
    }


}
