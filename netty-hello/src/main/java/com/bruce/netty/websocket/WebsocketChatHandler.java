package com.bruce.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Created by bruce on 2018/10/16 21:46
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
@Slf4j
public class WebsocketChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //记录所有的客户端
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public WebsocketChatHandler(){
        log.info("WebsocketChatHandler create");
    }

    /*
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channle，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        clients.add(ctx.channel());
        log.info("handlerAdded ");
        log.info(ctx.channel().id().asLongText());
        log.info(ctx.channel().id().asShortText());
        log.info("客户端连接当前数量:" + clients.size());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        //当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        //clients.remove(ctx.channel());
        log.info("handlerRemoved ");
        log.info(ctx.channel().id().asLongText());
        log.info(ctx.channel().id().asShortText());
        log.info("客户端连接剩余数量:" + clients.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        log.info("接收到的消息" + text);
        /*for (Channel client : clients) {
            if (client.id().asLongText().equals(ctx.channel().id().asLongText())) {
                //自己就不需要发送了
                continue;
            }
            client.writeAndFlush(new TextWebSocketFrame("服务端在" + LocalDateTime.now() + "接收到" + ctx.channel().id().asShortText() + "消息" + text));
        }*/
        clients.writeAndFlush(new TextWebSocketFrame("服务端在" + LocalDateTime.now() + "接收到" + ctx.channel().id().asShortText() + "消息" + text));
    }
}
