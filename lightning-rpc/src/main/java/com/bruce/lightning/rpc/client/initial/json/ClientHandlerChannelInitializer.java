package com.bruce.lightning.rpc.client.initial.json;

import com.bruce.lightning.rpc.client.handler.ClientHeartBeatHandler;
import com.bruce.lightning.rpc.client.handler.RpcClientHandler;
import com.bruce.lightning.rpc.common.AppendDelimiterOutboundHandler;
import com.bruce.lightning.rpc.common.serial.JsonEncodeHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientHandlerChannelInitializer extends ChannelInitializer<SocketChannel> {

    AppendDelimiterOutboundHandler appendDelimiterOutboundHandler = new AppendDelimiterOutboundHandler();
    StringDecoder stringDecoder = new StringDecoder();
    StringEncoder stringEncoder = new StringEncoder();

    ClientJsonDecodeHandler clientJsonDecodeHandler = new ClientJsonDecodeHandler();
    JsonEncodeHandler jsonEncodeHandler = new JsonEncodeHandler();

    ClientHeartBeatHandler clientHeartBeatHandler = new ClientHeartBeatHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //基于分隔符的流解析器,这里使用换行符
        pipeline.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()));
        pipeline.addLast(stringDecoder); //字符串解码器
        pipeline.addLast(stringEncoder); //字符串编码器
        pipeline.addLast(appendDelimiterOutboundHandler); //写出添加分隔符处理器

        pipeline.addLast(clientHeartBeatHandler);

        pipeline.addLast(clientJsonDecodeHandler);
        pipeline.addLast(jsonEncodeHandler);

        pipeline.addLast(new RpcClientHandler());
    }
}
