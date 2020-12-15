package com.bruce.lightning.rpc.common.serial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * <pre>
 * netty提供的Marshalling编解码器采用消息头和消息体的方式
 *
 * JBoss Marshalling是一个Java对象序列化包,对jdk默认的序列化框架进行优化
 * 但又保持跟Serializable接口的兼容,同时增加了一些可调用的参数和附加的特性
 * 经过测试发现序列化后的流较protostuff,MessagePack还是比较大的,
 * 序列化和反序列化的类必须是同一个类,否则:
 * io.netty.handler.codec.DecoderException: java.lang.ClassNotFoundException: lwl.gpsmap.entity.UserInfo
 *
 * maven 依赖:
 * jboss-marshalling 和 jboss-marshalling-serial
 * </pre>
 *
 * @author liwenlong - 2018/3/9 16:05
 */
public final class MarshallingCodeFactory {

    /**
     * 创建Jboss marshalling 解码器
     * @return
     */
    public static MyMarshallingDecoder buildMarshallingDecoder(){
        //参数serial表示创建的是Java序列化工厂对象,由jboss-marshalling-serial提供
        MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        DefaultUnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, configuration);

        return new MyMarshallingDecoder(provider,1024);
    }

    /**
     * 创建Jboss marshalling 编码器
     */
    public static MarshallingEncoder buildMarshallingEncoder(){
        MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        DefaultMarshallerProvider provider = new DefaultMarshallerProvider(factory, configuration);
        return new MarshallingEncoder(provider);
    }

    public static class MyMarshallingDecoder extends MarshallingDecoder{
        public MyMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize) {
            super(provider, maxObjectSize);
        }
        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            System.out.println("读取数据长度:" + in.readableBytes());
            return super.decode(ctx, in);
        }
    }




}
