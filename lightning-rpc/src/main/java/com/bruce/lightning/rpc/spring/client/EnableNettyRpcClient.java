package com.bruce.lightning.rpc.spring.client;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by bruce on 2019/1/11 21:32
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({NettyRpcClientAutoConfigRegistrar.class})
public @interface EnableNettyRpcClient {

    /** 服务提供者地址 */
    String host();

    /** 服务提供者端口 */
    int port() default 8088;


}
