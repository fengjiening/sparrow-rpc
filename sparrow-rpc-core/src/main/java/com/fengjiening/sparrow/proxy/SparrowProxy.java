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
    private static final CommandManager manager = new CommandManager();
    private static  final TcpClientApp client= new TcpClientApp();
    private static HashedWheelTimer timer = new HashedWheelTimer();

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
            final RpcResponse response = sendRequest(request);
            if(response.getException() != null){
                throw response.getException();
            }


            return response.getResult();
        });
    }
    /**
     * 发送请求
     * @param request 请求实体 {@link RpcRequest}
     * @return {@link RpcResponse}
     * @throws InterruptedException e
     */
    public static RpcResponse sendRequest(RpcRequest request) throws InterruptedException {
        String nextId = SparrowContext.snowFlake.nextId();
        RemotingCommand requestCommand = manager.createRequest(nextId,request);
        // 获取producer地址
        Url url = lookupProvider(request);
        if(url == null){
            return RpcResponse.builder()
                    .exception(new NullPointerException("No provider found for " + request.getType().getName()))
                    .build();
        }
        try{
            ConnectionManager connectionManager = new ConnectionManager(new SparrowChannelFactory(new ConnectEventHandler()));
            Connection connection = connectionManager.getConnectionAndCreateIfAbsent(url);

            // 发送请求，获得future
            DefaultInvokeFuture invokeFuture = sendFuture(connection, requestCommand, null);
            // 等待结果
            RemotingCommand responseCommand = (RemotingCommand) invokeFuture.awaitResponse();
            return processResponse(responseCommand);
        }catch (ConnectException e) {
            return RpcResponse.builder()
                    .exception(new RuntimeException("Connect to target url failed, url: " + url.getOriginalUrl()))
                    .build();
        }

    }
    /**
     * 处理收到的回复报文
     * 首先判断回复的命令类型，然后检查报文完整性
     * @param responseCommand {@link RemotingCommand}
     * @return {@link RpcResponse}
     */
    private static  RpcResponse processResponse(RemotingCommand responseCommand){
        //if(responseCommand.getCommandCode().equals(RpcProtocol.RESPONSE)){
        //    // 获得response
        //    byte[] content = responseCommand.getContent();
        //    // 检查CRC32校验码
        //    if(checkCrc32(content, responseCommand.getCrc32())){
        //        // 解压数据部分
        //        byte compressorCode = responseCommand.getCompressor();
        //        if(compressorCode != -1){
        //            Compressor compressor = CompressorManager.getCompressor(compressorCode);
        //            content = compressor.decompress(content);
        //        }
        //        // 反序列化
        //        Serializer serializer = SerializerManager.getSerializer(responseCommand.getSerializer());
        //        return serializer.deserialize(content, RpcResponse.class);
        //    }
        //    else{
        //        // CRC32 错误，报文损坏
        //        throw new RuntimeException("network packet damaged during transport");
        //    }
        //}else if(responseCommand.getCommandCode().equals(RpcProtocol.ERROR)){
        //    // 服务端回复Error
        //    throw new RuntimeException(new String(responseCommand.getContent(), StandardCharsets.UTF_8));
        //}else{
        //    // 请求Timeout
        //    throw new RuntimeException(new String(responseCommand.getContent(), StandardCharsets.UTF_8));
        //}
        return null;
    }
    /**
     * 查询服务提供者
     * @param request {@link RpcRequest}
     * @return 服务提供者地址
     */
    private static Url lookupProvider(RpcRequest request){
        // 本地缓存获取provider
        //Set<ProviderNode> providerNodes = localRegistry.lookUpProviders(request.getType().getName(), request.getVersion());
        //if(providerNodes == null || providerNodes.isEmpty()){
        //    return null;
        //}
        //ProviderNode provider = loadBalance.select(providerNodes, request);
        //return provider == null ? null : Url.parseString(provider.getUrl());
        Url url = new Url("127.0.0.1:8080");
        url.setIp("127.0.0.1");
        url.setPort(8080);
        return url;
    }

    public static DefaultInvokeFuture sendFuture(Connection connection, RemotingCommand command, InvokeCallback callback) {
        int commandId = 1;//command.getId();
        // create invoke future
        DefaultInvokeFuture future = new DefaultInvokeFuture(callback);
        // check if command already timeout, client fail-fast
        long timeout =System.nanoTime(); //command.getTimeoutMillis();
        if(System.currentTimeMillis() >= timeout){
            throw new RuntimeException("request timeout before sending, command: " + command);
        }

        timer.newTimeout(new TimeoutTask(command, connection, callback), (timeout - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
        // save invoke future
        connection.addInvokeFuture(commandId, future);
        // send request and listen send result
        connection.getChannel().writeAndFlush(command).addListener((ChannelFutureListener) listener->{
            if(!listener.isSuccess()){
                // failed to send
                log.warn("send failed, command: {}, canceled: {}, done: {}", command, listener.isCancelled(), listener.isDone());
                // put exception response
                RemotingCommand response = createExceptionResponse(commandId, "failed to send request to target address");
                future.putResponse(response);
                // remove invoke future
                connection.removeInvokeFuture(commandId);
                // callback
                if(callback != null){
                    callback.onComplete(response);
                }
            }
        });
        return future;
    }
    public static RemotingCommand createExceptionResponse(int id, String message) {
        RemotingCommand request = manager.createRequest(id + "", new Object());
        byte[] content = message.getBytes(StandardCharsets.UTF_8);
        return request;
    }
    public static RemotingCommand createTimeoutResponse(int id, Object data) {
        if(data instanceof String){
            String message = (String) data;
            byte[] content = message.getBytes(StandardCharsets.UTF_8);
            //return createHeader(id, RpcProtocol.TIMEOUT)
            //        .content(content)
            //        .length(RpcProtocol.HEADER_LENGTH + content.length)
            //        .crc32(crc32(content))
            //        .build();
        }
        return RemotingCommand.builder().build();
    }

    static class TimeoutTask implements TimerTask {
        private final RemotingCommand request;
        private final Connection connection;
        private final InvokeCallback callback;

        public TimeoutTask(RemotingCommand request, Connection connection, InvokeCallback callback) {
            this.request = request;
            this.connection = connection;
            this.callback = callback;
        }

        @Override
        public void run(Timeout timeout) {
            // remove timeout future
            DefaultInvokeFuture timeoutFuture = connection.removeInvokeFuture(1);
            if(timeoutFuture != null){
                // put timeout response
                timeoutFuture.putResponse(createTimeoutResponse(1, "await response timeout, request id: " + request.getId()));
                // execute callback using timer thread
                if(callback != null){
                    callback.onTimeout(request);
                }
            }
        }
    }



}
