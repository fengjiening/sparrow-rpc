package com.fengjiening.sparrow.config;

import com.fengjiening.sparrow.entity.RpcResponse;

/**
 * @ClassName: DefaultFuture
 * @Description: TODO
 * @Date: 2022/11/3 10:58
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class DefaultFuture {
    private RpcResponse rpcResponse;
    private volatile boolean isSucceed = false;
    private final Object object = new Object();

    public RpcResponse getRpcResponse(int timeout) {
        synchronized (object) {
            while (!isSucceed) {
                try {
                    object.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return rpcResponse;
        }
    }

    public void setResponse(RpcResponse response) {
        if (isSucceed) {
            return;
        }
        synchronized (object) {
            this.rpcResponse = response;
            this.isSucceed = true;
            object.notify();
        }
    }
}
