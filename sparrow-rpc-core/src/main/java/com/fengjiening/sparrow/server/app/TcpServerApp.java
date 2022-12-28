package com.fengjiening.sparrow.server.app;
import com.fengjiening.sparrow.config.ConnectEventHandler;
import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.manager.ConnectionManager;
import com.fengjiening.sparrow.pool.Connection;
import com.fengjiening.sparrow.pool.Url;
import com.fengjiening.sparrow.server.handle.NettyServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ServerApp
 * @Description: 服务端
 * @Date: 2022/10/25 20:37
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Slf4j
public class TcpServerApp {
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private final ServerBootstrap bootstrap;
    /**
     * server side connection manager
     */
    private ConnectionManager connectionManager;
    /**
     * connect event handler
     */
    private ConnectEventHandler connectEventHandler;

    /**
     * server port
     */
    private final int port;

    public TcpServerApp( int port) {
        this.bootstrap = new ServerBootstrap();
        //this.codec = codec;
        this.port = port;
        //this.baseRemoting = new BaseRemoting(commandFactory);
    }
    public void init(){
        this.boss = new NioEventLoopGroup(1);
        this.worker = new NioEventLoopGroup();

        this.connectionManager = new ConnectionManager();
        this.connectEventHandler = new ConnectEventHandler();
        // init server connection manage if necessary
        //if(DoveConfigs.serverManageConnection()){
        //    this.connectionManager = new ConnectionManager();
        //    this.connectEventHandler = new ConnectEventHandler();
        //}else{
        //    this.connectEventHandler.setConnectionManager(connectionManager);
        //    this.connectEventHandler = new ConnectEventHandler();
        //}
        // 设置线程池
        bootstrap.group(boss, worker);

        // 设置nioSocket工厂,和三有不同
        bootstrap.channel(NioServerSocketChannel.class);
        // 设置参数，TCP参数
        // serverSocketChannel的设置，连接缓冲池的大小，accept的最大连接数
        bootstrap.option(ChannelOption.SO_BACKLOG, 2048);
        // socketChannel的设置，维持连接的活跃，清除无用的死连接
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // socketChannel的设置，关闭延迟发送（就是关闭缓冲池）
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        bootstrap.handler(new LoggingHandler(LogLevel.INFO));
        // 设置管道工厂,和3也有不同
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                channel.pipeline().addLast(new LengthFieldPrepender(4));
                channel.pipeline().addLast(new SparrowEncoder());
                channel.pipeline().addLast( new SparrowDecoder());
                channel.pipeline().addLast( new NettyServerHandle());
                createConnection(channel);
            }
        });
        //// init channel
        //bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
        //    @Override
        //    protected void initChannel(NioSocketChannel channel) {
        //        ChannelPipeline pipeline = channel.pipeline();
        //        // SSL/TLS Handler
        //        if(DoveConfigs.enableSsl() && sslContext != null){
        //            // add SslHandler here
        //        }
        //        // protocol encoder and decoder
        //        pipeline.addLast("decoder", codec.newDecoder());
        //        pipeline.addLast("encoder", codec.newEncoder());
        //
        //        // connect event handler
        //        pipeline.addLast("connect-event-handler", DoveServer.this.connectEventHandler);
        //
        //        // add heart-beat handlers
        //        if(DoveConfigs.tcpIdleState()){
        //            pipeline.addLast("idle-state-handler", new IdleStateHandler(DoveConfigs.tcpIdleTime(), DoveConfigs.tcpIdleTime(), 0, TimeUnit.MILLISECONDS));
        //            pipeline.addLast("heart-beat-handler", new HeartBeatHandler());
        //        }
        //        // command handler
        //        pipeline.addLast("command-handler", new CommandChannelHandler());
        //
        //        // create connection instance and bind it with channel.
        //        createConnection(channel);
        //    }
        //});
    }

    private void createConnection(Channel channel){
        // parse url from SocketAddress
        Url url = Url.fromAddress((InetSocketAddress) channel.remoteAddress());
        new Connection(channel, url);
        //if(DoveConfigs.serverManageConnection()){
        //    // add connection to connection Manager
        //    this.connectionManager.add(new Connection(channel, url));
        //}else{
        //    // bind the connection instance with channel
        //    new Connection(channel, url);
        //}
    }

    public  void start() {
        long start = System.currentTimeMillis();
        try {
            init();
            ChannelFuture future = bootstrap.bind(port).sync();
            if(future.isSuccess()){
                log.info("server started, time used : {}ms", (System.currentTimeMillis() - start));
            }
            future.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("server start failed, ", e);
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void shutdown() {
        this.boss.shutdownGracefully();
        this.worker.shutdownGracefully();
    }
}
