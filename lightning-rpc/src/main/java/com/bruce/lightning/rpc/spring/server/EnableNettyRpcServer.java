package com.bruce.lightning.rpc.spring.server;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({NettyRpcServerAutoConfigRegistrar.class})
public @interface EnableNettyRpcServer {


    int port() default 8088;


}
