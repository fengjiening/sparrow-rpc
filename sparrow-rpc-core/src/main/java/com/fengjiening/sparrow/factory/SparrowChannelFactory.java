package com.fengjiening.sparrow.factory;

import com.fengjiening.sparrow.cilent.handle.NettyClientHandle;
import com.fengjiening.sparrow.config.ConnectEventHandler;
import com.fengjiening.sparrow.config.HeartBeatHandler;
import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.manager.ConnectionManager;
import com.fengjiening.sparrow.pool.Connection;
import com.fengjiening.sparrow.pool.Url;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultExecutorServiceFactory;
import io.netty.util.concurrent.ExecutorServiceFactory;
import io.netty.util.internal.StringUtil;
import org.springframework.util.ObjectUtils;

import java.net.ConnectException;

import static com.fengjiening.sparrow.config.protocol.SparrowProtocol.protocolCode;

/**
 * @ClassName: ChannelFactory
 * @Description: 通道工程
 * @Date: 2022/11/1 11:22
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowChannelFactory {


    /**
     * heart-beat handler
     */
    private final ChannelHandler heartBeatHandler;
    /**
     * connect event handler
     */
    private final ConnectEventHandler connectEventHandler;

    private Bootstrap bootstrap;
    private final EventLoopGroup worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1,
            new DefaultExecutorServiceFactory("Sparrow-Channel-"));

    public SparrowChannelFactory( ConnectEventHandler connectEventHandler) {
        this.heartBeatHandler = new HeartBeatHandler();
        this.connectEventHandler = new ConnectEventHandler();

    }

    /**
     * init client bootstrap
     */
    public void init(){
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(worker);

        // options 采用一定算法 小包合成大包发送
        //bootstrap.option(ChannelOption.TCP_NODELAY, DoveConfigs.tcpNoDelay());

        // register handlers
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", new SparrowDecoder());
                pipeline.addLast("encoder", new SparrowEncoder());
                // connect event handler
                pipeline.addLast("connect-event-handler", connectEventHandler);

                // heart-beat handler
                //if(DoveConfigs.tcpIdleState()){
                if(true){
                   // pipeline.addLast("idle-state-handler", new IdleStateHandler(DoveConfigs.tcpIdleTime(), DoveConfigs.tcpIdleTime(), 0));
                    //pipeline.addLast("heart-beat-handler", heartBeatHandler);
                }
                // add response handler, handles invoke future
                pipeline.addLast("dove-response-handler", new NettyClientHandle());

            }
        });
    }


    public Connection create(Url url, int timeout) throws IllegalArgumentException, ConnectException {
        // check arguments
        if(ObjectUtils.isEmpty(url.getIp()) || url.getPort() <= 0){
            throw new IllegalArgumentException("invalid socket address");
        }
        if(timeout <= 0){
            throw new IllegalArgumentException("connect timeout must be positive");
        }
        io.netty.channel.Channel channel = doCreateConnection(url.getIp(), url.getPort(), timeout);
        return new Connection(channel, protocolCode, url);
    }

    /**
     * establish connection and returns the target channel
     * @param ip ip
     * @param port port
     * @param timeout timeout ms
     * @return {@link io.netty.channel.Channel}
     * @throws ConnectException connect errors
     */
    private Channel doCreateConnection(String ip, int port, int timeout) throws ConnectException{
        // set connect timeout
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
        ChannelFuture future = bootstrap.connect(ip, port);

        // wait for result
        future.awaitUninterruptibly();

        if(!future.isDone()){
            // connect timeout
            throw new ConnectException("connect timeout, target address: " + ip + ":" + port);
        }
        if(future.isCancelled()){
            // connect task cancelled
            throw new ConnectException("connect cancelled");
        }
        if(!future.isSuccess()){
            // error
            throw new ConnectException("connect error");
        }

        return future.channel();
    }

    public void shutdown(){
        this.worker.shutdownGracefully();
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectEventHandler.setConnectionManager(connectionManager);
    }

}
