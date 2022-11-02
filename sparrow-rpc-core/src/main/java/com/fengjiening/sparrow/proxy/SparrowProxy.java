package com.fengjiening.sparrow.proxy;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.entity.RpcRequest;
import com.fengjiening.sparrow.entity.RpcResponse;
import com.fengjiening.sparrow.manager.CilentManager;
import com.fengjiening.sparrow.manager.CommandManager;
import com.fengjiening.sparrow.utill.SnowFlake;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: SparrowProxy
 * @Description: TODO
 * @Date: 2022/11/2 11:37
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowProxy {
    private static final CommandManager manager = new CommandManager();
    private static SnowFlake snowFlake = new SnowFlake(0, 0);

    public static Object instance(Class<?> targetClass, int version){
        String nextId = snowFlake.nextId();
        return Proxy.newProxyInstance(SparrowProxy.class.getClassLoader(), new Class[]{targetClass}, (proxy, method, args) -> {
            // 创建request
            RpcRequest request = RpcRequest.builder().methodName(method.getName())
                    .parameters(args)
                    .parameterTypes(method.getParameterTypes())
                    .version(version)
                    .type(targetClass)
                    .build();
            RemotingCommand requestCommand = manager.createRequest(nextId,request);
            // 发送请求
            final RpcResponse response = CilentManager.send(requestCommand);
            if(response.getException() != null){
                throw response.getException();
            }
            return response.getResult();
        });
    }
}
