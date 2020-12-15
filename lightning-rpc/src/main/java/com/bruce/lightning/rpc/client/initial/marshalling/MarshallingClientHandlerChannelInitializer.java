package com.bruce.lightning.rpc.client.initial.marshalling;

import com.bruce.lightning.rpc.client.handler.HeartBeatPongHandler;
import com.bruce.lightning.rpc.client.handler.RpcClientHandler;
import com.bruce.lightning.rpc.common.serial.MarshallingCodeFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class MarshallingClientHandlerChannelInitializer extends ChannelInitializer<SocketChannel> {

    HeartBeatPongHandler heartBeatPongHandler = new HeartBeatPongHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
        pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());

        pipeline.addLast(new IdleStateHandler(0, 0, 5));
        pipeline.addLast(heartBeatPongHandler);

        pipeline.addLast(new RpcClientHandler());
    }
}
