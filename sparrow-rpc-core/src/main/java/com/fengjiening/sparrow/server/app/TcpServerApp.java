package com.fengjiening.sparrow.server.app;
import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.server.handle.NettyServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @ClassName: ServerApp
 * @Description: 服务端
 * @Date: 2022/10/25 20:37
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class TcpServerApp {

    public  void start() throws InterruptedException {
        // 创建一个服务类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 创建woker和boss线程池组
        // netty5创建的并不是连接池，而是用本身封装好的方法，其实现是ScheduledExecutor连接池
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 设置线程池
            serverBootstrap.group(boss, worker);

            // 设置nioSocket工厂,和三有不同
            serverBootstrap.channel(NioServerSocketChannel.class);
            // 设置参数，TCP参数
            // serverSocketChannel的设置，连接缓冲池的大小，accept的最大连接数
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);
            // socketChannel的设置，维持连接的活跃，清除无用的死连接
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // socketChannel的设置，关闭延迟发送（就是关闭缓冲池）
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);

            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            // 设置管道工厂,和3也有不同
            serverBootstrap.childHandler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                    channel.pipeline().addLast(new LengthFieldPrepender(4));
                    channel.pipeline().addLast(new SparrowEncoder());
                    channel.pipeline().addLast( new SparrowDecoder());
                    channel.pipeline().addLast( new NettyServerHandle());
                }
            });

            // 绑定端口
            ChannelFuture future = serverBootstrap.bind(10101);
            System.out.println("服务端正常启动！！");
            // 等待服务端关闭, 该方法会阻塞在这里, 关闭后执行
            // 该管道是serverSocketChannel
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭boss和worker
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
