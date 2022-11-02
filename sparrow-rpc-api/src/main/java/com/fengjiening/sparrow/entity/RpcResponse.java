package com.fengjiening.sparrow.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 函数返回值
     */
    private Object result;
    /**
     * 抛出异常
     */
    private Throwable exception;
}
