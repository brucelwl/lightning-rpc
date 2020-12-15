package com.bruce.lightning.rpc.client;

import com.bruce.lightning.rpc.common.RpcRequest;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {

    private static final AtomicLong aid = new AtomicLong();

    private final long id;

    public static AtomicLong getAid() {
        return aid;
    }

    private Object[] args;
    private String fullMethodName;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }

    private RpcRequest rpcRequest;

    public ClientRequest() {
        this.id = aid.incrementAndGet();
        rpcRequest = new RpcRequest();
    }

    public RpcRequest build() {
        rpcRequest.setArgs(args);
        rpcRequest.setFullMethodName(fullMethodName);
        rpcRequest.setId(id);
        return rpcRequest;
    }
}
