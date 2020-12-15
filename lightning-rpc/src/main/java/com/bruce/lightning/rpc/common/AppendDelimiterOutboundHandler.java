package com.bruce.lightning.rpc.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 写出字符串数据之前添加分隔符,线程安全
 */
@ChannelHandler.Sharable
public class AppendDelimiterOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg + System.lineSeparator(), promise);
    }

}
