package com.bruce.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * Created by bruce on 2018/10/25 10:24
 */
public class DispatcherHandler extends ChannelInboundHandlerAdapter {
    private static final boolean checkStartsWith = false;
    private static final String websocketPath = "/ws";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChannelPipeline pipeline = ctx.pipeline();
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            if (isNotWebSocketPath(req) && pipeline.get("httpHandler") == null) {
                pipeline.addLast("httpHandler", new HttpHandler());
            } else {
                pipeline.addLast(new WebSocketServerProtocolHandler(websocketPath));
                pipeline.addLast("websocketChatHandler", new WebsocketChatHandler());
            }

            pipeline.remove("dispatcherHandler");
        }

        super.channelRead(ctx, msg);
    }

    private boolean isNotWebSocketPath(FullHttpRequest req) {
        return checkStartsWith ? !req.uri().startsWith(websocketPath) : !req.uri().equals(websocketPath);
    }
}
