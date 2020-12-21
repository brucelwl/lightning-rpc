package com.bruce.lightning.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * 只能在rpc 处理线程中使用
 */
public class RpcContext {

    private static final FastThreadLocal<ChannelHandlerContext> contextFastThreadLocal = new FastThreadLocal<>();

    public static void set(ChannelHandlerContext ctx) {
        contextFastThreadLocal.set(ctx);
    }

    public static ChannelHandlerContext get() {
        return contextFastThreadLocal.get();
    }

    public static void remove() {
        contextFastThreadLocal.remove();
    }

}
