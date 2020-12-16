package com.bruce.lightning.rpc.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 如果客户端接收到的是ping请求,则直接回复pong
 */
@ChannelHandler.Sharable
public class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ClientHeartBeatHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ("ping".equals(msg)) {
            ctx.writeAndFlush("pong");
            log.info("接收到服务端的心跳检测:{}", LocalDateTime.now().toString());
        } else if ("pong".equals(msg)) {
            log.info("接收到服务端的pong:{}", LocalDateTime.now().toString());
        } else {
            super.channelRead(ctx, msg);
        }

    }

    //事件处理.
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //该事件需要配合 io.netty.handler.timeout.IdleStateHandler使用
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                //向服务端发送心跳检测
                ctx.writeAndFlush("ping");
            } else if (idleStateEvent.state() == IdleState.READER_IDLE) {
                //超过指定时间没有读事件,关闭连接
                log.info("超过心跳时间,关闭和服务端的连接:{}", ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }


}
