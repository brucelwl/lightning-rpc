package com.bruce.lightning.rpc.server.initial.json;

import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.server.mapping.BeanMethod;
import com.bruce.lightning.rpc.server.mapping.BeanMethodContext;
import com.bruce.lightning.rpc.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ServerJsonDecodeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RpcRequest request = JsonUtils.jsonToObj(msg.toString(), RpcRequest.class);

        BeanMethod beanMethod = BeanMethodContext.getRpcBeanMap().get(request.getFullMethodName());
        Method method = beanMethod.getMethod();
        Object[] args = request.getArgs();

        //TODO 将json默认的对象转为具体的实体类
        //消费端User到服务提供端后可能是LinkedHashMap, 需要再转一下转成User
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Object[] objects = new Object[genericParameterTypes.length];
        for (int i = 0; i < genericParameterTypes.length; i++) {
            Object arg = JsonUtils.parse(JsonUtils.toJson(args[i]), genericParameterTypes[i]);
            objects[i] =arg;
        }
        //替换为真实的args
        request.setArgs(objects);

        super.channelRead(ctx, request);
    }
}
