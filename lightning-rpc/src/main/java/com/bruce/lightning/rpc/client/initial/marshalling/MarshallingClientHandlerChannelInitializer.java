package com.bruce.lightning.rpc.client.initial.marshalling;

import com.bruce.lightning.rpc.client.handler.ClientHeartBeatHandler;
import com.bruce.lightning.rpc.client.handler.RpcClientHandler;
import com.bruce.lightning.rpc.common.serial.MarshallingCodeFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class MarshallingClientHandlerChannelInitializer extends ChannelInitializer<SocketChannel> {

    ClientHeartBeatHandler clientHeartBeatHandler = new ClientHeartBeatHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
        pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());

        pipeline.addLast(new IdleStateHandler(60, 0, 25));
        pipeline.addLast(clientHeartBeatHandler);

        pipeline.addLast(new RpcClientHandler());
    }
}
