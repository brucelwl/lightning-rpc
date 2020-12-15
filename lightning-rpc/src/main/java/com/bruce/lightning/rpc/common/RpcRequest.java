package com.bruce.lightning.rpc.common;


import java.io.Serializable;

public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 3664262721774937144L;

    private Long id;
    private String fullMethodName;
    private Object[] args;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
