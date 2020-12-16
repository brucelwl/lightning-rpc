package com.bruce.lightning.rpc.server.initial.marshalling;

import com.bruce.lightning.rpc.common.serial.MarshallingCodeFactory;
import com.bruce.lightning.rpc.server.handler.ServerHeartBeatHandler;
import com.bruce.lightning.rpc.server.handler.RpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class MarshallingServerHandlerChannelInitializer extends ChannelInitializer<SocketChannel> {

    ServerHeartBeatHandler serverHeartBeatHandler = new ServerHeartBeatHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
        pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());

        //心跳检测机制(这里采用服务端向客户端发送ping机制)
        pipeline.addLast(new IdleStateHandler(25, 0, 10));
        pipeline.addLast(serverHeartBeatHandler);

        pipeline.addLast(new RpcServerHandler());
    }
}
