package com.bruce.lightning.rpc.common;

/**
 * Created by bruce on 2019/1/12 15:10
 */
public class RpcTimeoutException extends RuntimeException {

    private static final long serialVersionUID = 799719113754567602L;

    public RpcTimeoutException(String message) {
        super(message);
    }
}
