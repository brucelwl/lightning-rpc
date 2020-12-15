package com.bruce.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by bruce on 2018/10/16 19:06
 */
@Slf4j
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public HttpHandler() {
        log.info("HttpHandler create");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        Channel channel = ctx.channel();
        //打印客户端的远程地址
        log.info("客户端:" + channel.remoteAddress().toString() + " 请求路径:" + request.uri());
        ByteBuf content = Unpooled.copiedBuffer("hello netty--", CharsetUtil.UTF_8);
        //返回客户端数据
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        //设置响应头
        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        headers.set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ChannelFuture future = ctx.writeAndFlush(response);
        //加上下面代码后,每次请求结束,该handler就被销毁,下次请求重新实例化
        //future.addListener(ChannelFutureListener.CLOSE);
    }


}
