package com.fengjiening.sparrow.cilent.app;

import com.fengjiening.sparrow.config.ConnectEventHandler;
import com.fengjiening.sparrow.config.SparrowPlatform;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.entity.RpcRequest;
import com.fengjiening.sparrow.entity.RpcResponse;
import com.fengjiening.sparrow.exception.SparrowCode;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.factory.SparrowChannelFactory;
import com.fengjiening.sparrow.loadbalance.LoadBalance;
import com.fengjiening.sparrow.manager.CommandManager;
import com.fengjiening.sparrow.manager.ConnectionManager;
import com.fengjiening.sparrow.pool.Connection;
import com.fengjiening.sparrow.pool.DefaultInvokeFuture;
import com.fengjiening.sparrow.pool.InvokeCallback;
import com.fengjiening.sparrow.pool.Url;
import com.fengjiening.sparrow.protocol.local.LocalRegistry;
import com.fengjiening.sparrow.registry.ProviderNode;
import com.fengjiening.sparrow.registry.Registry;
import com.fengjiening.sparrow.spi.ExtensionLoader;
import com.fengjiening.sparrow.utill.PropertiesUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName: ClientApp
 * @Description: 客户端连接
 * @Date: 2022/10/25 20:28
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Slf4j
public class TcpClientApp extends SparrowPlatform {

    /**
     * 本地注册中心缓存
     */
    private  LocalRegistry localRegistry;
    private static final CommandManager manager = new CommandManager();
    private static HashedWheelTimer timer = new HashedWheelTimer();
    /**
     * 负载均衡器
     */
    private LoadBalance loadBalance;
    private int maxConnections;

    public TcpClientApp() {
        this.localRegistry = new LocalRegistry();
        setUp();
    }
    @Override
    public void setUp() {
        String registryType =   PropertiesUtil.get("sparrow.registry.type","zookeeper");
        String loadBalanceType =PropertiesUtil.get("sparrow.client.load-balance","random");
        this.maxConnections =PropertiesUtil.getInt("sparrow.client.max-conn",5);
        if(StringUtils.isEmpty(registryType)){
            throw new SparrowException(new SparrowCode(CommonConstant.PROPERTIES_NOT_FIND_CODE),"sparrow.registry.typenot find");
        }else{
            ExtensionLoader<Registry> registryLoader = ExtensionLoader.getExtensionLoader(Registry.class);
            /*
             * 远程注册中心客户端
             */
            Registry registry = registryLoader.getExtension(registryType);

            // 初始化本地注册中心
            this.localRegistry.setRemoteRegistry(registry);
            // 初始化远程注册中心客户端
            registry.init();
            registry.setLocalRegistry(localRegistry);
        }
        // 加载负载均衡器
        ExtensionLoader<LoadBalance> loadBalanceLoader = ExtensionLoader.getExtensionLoader(LoadBalance.class);
        this.loadBalance = loadBalanceLoader.getExtension(loadBalanceType);
    }

    @Override
    public void startUp()  {

    }

    @Override
    public void shutDown() {
        //this.connectionManager.shutdown();
    }
    /**
     * 发送请求
     * @param request 请求实体 {@link RpcRequest}
     * @return {@link RpcResponse}
     * @throws InterruptedException e
     */
    public RpcResponse sendRequest(RpcRequest request) throws InterruptedException {
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
     * 查询服务提供者
     * @param request {@link RpcRequest}
     * @return 服务提供者地址
     */
    private  Url lookupProvider(RpcRequest request){
        // 本地缓存获取provider
        Set<ProviderNode> providerNodes = this.localRegistry.lookUpProviders(request.getType().getName(), request.getVersion());
        if(providerNodes == null || providerNodes.isEmpty()){
            return null;
        }
        ProviderNode provider = loadBalance.select(providerNodes, request);
        //ProviderNode provider = providerNodes.iterator().next();
        return provider == null ? null : Url.parseString(provider.getUrl());
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
