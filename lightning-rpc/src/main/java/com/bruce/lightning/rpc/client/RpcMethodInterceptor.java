package com.bruce.lightning.rpc.client;

import com.bruce.lightning.rpc.common.RpcMethodUtil;
import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.common.RpcResponse;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RpcMethodInterceptor implements InvocationHandler {

    private Class<?> rpcInterface;

    private int timeout;

    private LightningClient client;

    public RpcMethodInterceptor(Class<?> rpcInterface, int timeout, LightningClient client) {
        Objects.requireNonNull(rpcInterface);
        this.rpcInterface = rpcInterface;
        this.timeout = timeout;
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(Object.class)) {
            if (method.getName().equals("toString")) {
                return java.text.MessageFormat.format("serverAddress {0} ,rpc interface {1}", client.getRemoteAddress(), rpcInterface.getName());
            }
            throw new OperationNotSupportedException("rpc 接口不支持调用Object父类方法");
        }
        ClientRequest clientRequest = new ClientRequest();
        //设置请求参数
        clientRequest.setArgs(args);
        clientRequest.setFullMethodName(RpcMethodUtil.getFullMethodName(method));
        RpcRequest request = clientRequest.build();
        DefaultResponseFuture responseFuture = client.send(request);
        // <= 0 采用默认值
        RpcResponse response = timeout <= 0
                ? responseFuture.get()
                : responseFuture.get(timeout, TimeUnit.MILLISECONDS);

        return response.getResult();
    }
}