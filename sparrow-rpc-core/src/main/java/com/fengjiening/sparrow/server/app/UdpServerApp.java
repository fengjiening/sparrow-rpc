package com.fengjiening.sparrow.server.app;

import com.fengjiening.sparrow.config.SparrowPlatform;
import com.fengjiening.sparrow.server.handle.NettyUpdServerHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @ClassName: ServerApp
 * @Description: 服务端
 * @Date: 2022/10/25 20:37
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class UdpServerApp extends SparrowPlatform {


    @Override
    public void setUp() {
        //先去找服务地址

        //然后
    }

    @Override
    public void startUp()  {

    }

    @Override
    public void shutDown() {

    }

    public  void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    // 主线程处理
                    .channel(NioDatagramChannel.class)
                    // 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    // 设置读缓冲区为2M
                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
                    // 设置写缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {

                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NioEventLoopGroup(), new NettyUpdServerHandle());
                        }
                    });

            ChannelFuture f = bootstrap.bind(8088).sync();
            System.out.println("服务器正在监听......");
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
