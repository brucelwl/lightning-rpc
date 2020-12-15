package com.bruce.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * Created by bruce on 2018/10/16 21:25
 */
public class WebsocketInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //websocket基于http,需要http编解码器
        pipeline.addLast(new HttpServerCodec());
        //对大数据流读写的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合,聚合成FullHttpRequest或者FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1048576));
        /* 添加websocket支持
         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
         * 本handler会帮你处理一些繁重的复杂的事
         * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
         * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
         */
        //pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast("dispatcherHandler", new DispatcherHandler());
        // pipeline.addLast(new WebsocketChatHandler());
        //pipeline.addLast(new HttpHandler());
    }


}
