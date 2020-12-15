package com.bruce.lightning.rpc.server.mapping;


import java.lang.reflect.Method;

public class BeanMethod {

    public BeanMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    private Object bean;

    private Method method;

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

}
