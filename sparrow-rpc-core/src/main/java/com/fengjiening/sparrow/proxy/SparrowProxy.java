package com.fengjiening.sparrow.proxy;

import com.fengjiening.sparrow.cilent.app.TcpClientApp;
import com.fengjiening.sparrow.config.ConnectEventHandler;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.entity.RpcRequest;
import com.fengjiening.sparrow.entity.RpcResponse;
import com.fengjiening.sparrow.factory.SparrowChannelFactory;
import com.fengjiening.sparrow.manager.CommandManager;
import com.fengjiening.sparrow.manager.ConnectionManager;
import com.fengjiening.sparrow.pool.Connection;
import com.fengjiening.sparrow.pool.DefaultInvokeFuture;
import com.fengjiening.sparrow.pool.InvokeCallback;
import com.fengjiening.sparrow.pool.Url;
import com.fengjiening.sparrow.registry.ProviderNode;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SparrowProxy
 * @Description: TODO
 * @Date: 2022/11/2 11:37
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Slf4j
public class SparrowProxy {

    private static  final TcpClientApp client= new TcpClientApp();


    public static Object instance(Class<?> targetClass, int version){
        return Proxy.newProxyInstance(SparrowProxy.class.getClassLoader(), new Class[]{targetClass}, (proxy, method, args) -> {
            // 创建request
            RpcRequest request = RpcRequest.builder().methodName(method.getName())
                    .parameters(args)
                    .parameterTypes(method.getParameterTypes())
                    .version(version)
                    .type(targetClass)
                    .build();

            // 发送请求
            final RpcResponse response = client.sendRequest(request);
            if(response.getException() != null){
                throw response.getException();
            }
            return response.getResult();
        });
    }



}
