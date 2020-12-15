package com.bruce.lightning.rpc.server.handler;

import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.common.RpcResponse;
import com.bruce.lightning.rpc.server.mapping.BeanMethodContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(RpcServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("服务端接收:{}", msg);

        RpcRequest request = (RpcRequest) msg;
        //TODO 应该在异步线程中执行业务逻辑处理
        RpcResponse response = BeanMethodContext.process(request);

        ctx.writeAndFlush(response);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof java.io.IOException) {
            log.warn("远程主机强迫关闭了一个现有的连接,{}", ctx.channel().remoteAddress());
        } else {
            cause.printStackTrace();
        }
    }
}
