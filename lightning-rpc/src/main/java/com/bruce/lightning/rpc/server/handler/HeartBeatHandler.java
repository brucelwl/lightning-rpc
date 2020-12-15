package com.bruce.lightning.rpc.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *     心跳检查,如果指定时间内都没有读写请求,会执行userEventTriggered方法,触发IdleStateEvent事件
 *
 *     服务端"读"事件,代表客户端向服务端发送数据
 *     服务端"写"事件,代表服务端向客户端发送数据
 *     服务端"读写"事件,双向发送数据
 *
 *     如果服务端在10s内没有收到"读写"事件,则向客户端发送心跳
 *     如果服务端在20s内没有收到"读"事件,则认为客户端断开连接,关闭与客户端的连接
 *
 *     所以注意: "读写"检测的时间需要小于"读"检测时间,否则在没有发送心跳之前channel可能已经被关闭
 *
 *     另一种服务端心跳机制: 在指定时间内如果没有"读写"事件,则发送心跳检测,
 *     通过计数器统计发送的心跳的次数,超过指定次数则断开与客户端的连接
 *
 * </pre>
 */
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HeartBeatHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ("pong".equals(msg)) {
            log.info("接收到客户端{}的pong", ctx.channel().remoteAddress());
        } else if ("ping".equals(msg)) {
            ctx.writeAndFlush("pong");
            log.info("接收到客户端{}的ping", ctx.channel().remoteAddress());
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
                //向客户端发送心跳检测
                ctx.writeAndFlush("ping");
            } else if (idleStateEvent.state() == IdleState.READER_IDLE) {
                //超过指定时间没有读事件,关闭连接
                ctx.channel().close();
                log.info("READER_IDLE 关闭远程客户端{}", ctx.channel().remoteAddress());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

}
