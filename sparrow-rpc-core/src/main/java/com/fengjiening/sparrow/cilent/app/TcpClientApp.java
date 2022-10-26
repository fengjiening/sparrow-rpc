package com.fengjiening.sparrow.cilent.app;

import com.fengjiening.sparrow.cilent.handle.NettyClientHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * @ClassName: ClientApp
 * @Description: 客户端连接
 * @Date: 2022/10/25 20:28
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class TcpClientApp {

    public  void start() throws InterruptedException {
        // 服务类
        Bootstrap bootstrap = new Bootstrap();
        // worker
        // 这里只建worker用来监听数据的交互，因为是客户端，所以无需监听端口，监听accept操作
        EventLoopGroup worker = new NioEventLoopGroup();
        try {

            // 设置线程池
            bootstrap.group(worker);
            // 设置socket工厂
            bootstrap.channel(NioSocketChannel.class);
            // 设置管道
            bootstrap.handler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast("encoder", new StringEncoder());
                    channel.pipeline().addLast("decoder", new StringDecoder());
                    channel.pipeline().addLast("clientHandler", new NettyClientHandle());
                }
            });

            // 连接服务端
            ChannelFuture future = bootstrap.connect("127.0.0.1", 10101);
            // 得到通道，给服务端写数据
            Channel channel = future.channel();
            channel.writeAndFlush("hello");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                String line = bufferedReader.readLine();
                if(!"stop".equals(line)){
                    System.out.println("请输入：");
                    future.channel().writeAndFlush(line);
                }
                else {
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
