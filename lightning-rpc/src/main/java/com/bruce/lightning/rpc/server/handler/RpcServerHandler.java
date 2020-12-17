package com.bruce.lightning.rpc.server.handler;

import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.common.RpcResponse;
import com.bruce.lightning.rpc.server.mapping.BeanMethodContext;
import com.bruce.lightning.rpc.util.ReflectUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(RpcServerHandler.class);

    static final DefaultEventExecutorGroup eventExecutors;

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    static {
        DefaultThreadFactory threadFactory = new DefaultThreadFactory("RpcServerHandler-");
        ReflectUtils.set(threadFactory, DefaultThreadFactory.class, "prefix", "RpcServerHandler-");

        eventExecutors = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors() * 10, threadFactory);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("服务端接收:{}", msg);
        RpcRequest request = (RpcRequest) msg;

        //TODO 接收请求之后,应该在异步线程中执行
        eventExecutors.execute(new Runnable() {
            @Override
            public void run() {
                RpcResponse response = BeanMethodContext.process(request);
                ctx.writeAndFlush(response);

                log.info("服务端响应:{}", request.getId());
            }
        });


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("远程主机主动关闭了连接,{}", ctx.channel().remoteAddress());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof java.io.IOException) {
            log.warn("远程主机强迫关闭了一个现有的连接,{}", ctx.channel().remoteAddress());
        } else {
            cause.printStackTrace();
        }
        ctx.close();
    }
}
