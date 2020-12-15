package com.bruce.lightning.rpc.client.handler;

import com.bruce.lightning.rpc.client.DefaultResponseFuture;
import com.bruce.lightning.rpc.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(RpcClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("客户端接收:{}", msg);

        DefaultResponseFuture.receive((RpcResponse)msg);
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
