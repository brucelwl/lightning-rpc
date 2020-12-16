package com.bruce.lightning.rpc.server.initial.json;

import com.bruce.lightning.rpc.common.AppendDelimiterOutboundHandler;
import com.bruce.lightning.rpc.common.serial.JsonEncodeHandler;
import com.bruce.lightning.rpc.server.handler.ServerHeartBeatHandler;
import com.bruce.lightning.rpc.server.handler.RpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerHandlerChannelInitializer extends ChannelInitializer<SocketChannel> {

    AppendDelimiterOutboundHandler appendDelimiterOutboundHandler = new AppendDelimiterOutboundHandler();
    StringDecoder stringDecoder = new StringDecoder();
    StringEncoder stringEncoder = new StringEncoder();

    JsonEncodeHandler jsonEncodeHandler = new JsonEncodeHandler();
    ServerJsonDecodeHandler serverJsonDecodeHandler = new ServerJsonDecodeHandler();

    ServerHeartBeatHandler serverHeartBeatHandler = new ServerHeartBeatHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()));
        pipeline.addLast(stringDecoder); //字符串解码
        pipeline.addLast(stringEncoder); //字符串编码
        pipeline.addLast(appendDelimiterOutboundHandler); //写出添加分隔符处理器

        //心跳检测机制(这里采用服务端向客户端发送ping机制)
        pipeline.addLast(new IdleStateHandler(25, 0, 10));
        pipeline.addLast(serverHeartBeatHandler);

        pipeline.addLast(serverJsonDecodeHandler);
        pipeline.addLast(jsonEncodeHandler);

        pipeline.addLast(new RpcServerHandler());
    }
}