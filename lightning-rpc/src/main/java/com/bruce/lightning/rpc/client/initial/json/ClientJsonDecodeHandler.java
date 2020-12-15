package com.bruce.lightning.rpc.client.initial.json;

import com.bruce.lightning.rpc.common.RpcResponse;
import com.bruce.lightning.rpc.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientJsonDecodeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RpcResponse response = JsonUtils.jsonToObj(msg.toString(), RpcResponse.class);

        super.channelRead(ctx, response);
    }
}
