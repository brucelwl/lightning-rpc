package com.bruce.lightning.rpc.client;

import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.common.RpcResponse;
import com.bruce.lightning.rpc.common.RpcRemoteServiceException;
import com.bruce.lightning.rpc.common.RpcTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultResponseFuture {

    private static final Logger log = LoggerFactory.getLogger(DefaultResponseFuture.class);

    private static final ConcurrentHashMap<Long, DefaultResponseFuture> allResponseFuture = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private RpcResponse response;
    private RpcRequest request;

    public DefaultResponseFuture(RpcRequest request) {
        this.request = request;
        allResponseFuture.put(this.request.getId(), this);


    }

    public RpcResponse getResponse() {
        return response;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
    }

    /** 默认超时时间3S */
    public RpcResponse get() {
        return get(30000, TimeUnit.SECONDS);
    }

    public RpcResponse get(long time, TimeUnit timeUnit) {
        boolean timeout = false;
        try {
            lock.lock();
            while (!done()) {
                timeout = !condition.await(time, timeUnit);
                if (timeout) {
                    //从等待返回的集合中移除
                    allResponseFuture.remove(this.request.getId());
                    break;
                }
            }
        } catch (Exception e) {
            log.error("等待获取返回值出现异常:", e);
        } finally {
            lock.unlock();
        }
        if (timeout) {
            log.info("rpc 请求超时,{}毫秒后未返回结果!", TimeUnit.MILLISECONDS.convert(time, timeUnit));
            throw new RpcTimeoutException("rpc 请求超时," + TimeUnit.MILLISECONDS.convert(time, timeUnit) + "毫秒后未返回结果");
        }
        if (this.response.getThrowable() != null) {
            log.error("请求出现异常:", this.response.getThrowable());
            throw new RuntimeException(this.response.getThrowable().getMessage());
        }
        if (this.response.getErrorMesg() != null) {
            throw new RpcRemoteServiceException(this.response.getErrorMesg());
        }
        return this.response;
    }


    private boolean done() {
        return this.response != null;
    }

    /**
     * 在handler中获取返回值,并调用该方法
     *
     * @param response
     */
    public static void receive(RpcResponse response) {
        DefaultResponseFuture responseFuture = allResponseFuture.get(response.getRequestId());
        if (responseFuture != null) {
            ReentrantLock lock = responseFuture.lock;
            try {
                lock.lock();
                responseFuture.setResponse(response);
                responseFuture.condition.signal();

                //获取返回值以后移除
                allResponseFuture.remove(response.getRequestId());

            } catch (Exception e) {
                log.error("线程唤起出现异常:", e);
            } finally {
                lock.unlock();
            }
        }
    }


}
