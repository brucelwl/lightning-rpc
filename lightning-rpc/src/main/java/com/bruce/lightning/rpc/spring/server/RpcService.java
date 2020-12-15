package com.bruce.lightning.rpc.spring.server;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by bruce on 2019/1/11 19:20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
