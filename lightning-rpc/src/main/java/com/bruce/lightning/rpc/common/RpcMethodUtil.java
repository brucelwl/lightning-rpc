package com.bruce.lightning.rpc.common;

import java.lang.reflect.Method;

/**
 * Created by bruce on 2019/1/11 22:30
 */
public class RpcMethodUtil {

    public static String getFullMethodName(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return declaringClass.getName() + "." + method.getName();
    }


}
