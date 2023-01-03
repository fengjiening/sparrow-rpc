package com.fengjiening.sparrow.cilent.app;

import com.fengjiening.sparrow.cilent.handle.NettyClientHandle;
import com.fengjiening.sparrow.cilent.handle.NettyUdpClientHandle;
import com.fengjiening.sparrow.config.SparrowPlatform;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
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
public class UdpClientApp extends SparrowPlatform {


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

    public static void main1(String []a) throws InterruptedException {
        // 服务类
        Bootstrap bootstrap = new Bootstrap();
        // worker
        // 这里只建worker用来监听数据的交互，因为是客户端，所以无需监听端口，监听accept操作
        EventLoopGroup worker = new NioEventLoopGroup();
        try {

            // 设置线程池
            bootstrap.group(worker);
            // 设置socket工厂
            bootstrap.channel(NioDatagramChannel.class);
            // 设置管道
            bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {

                @Override
                protected void initChannel(NioDatagramChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new NioEventLoopGroup(), new NettyUdpClientHandle());
                }
            });

            // 连接服务端
            ChannelFuture future = bootstrap.connect("127.0.0.1", 10101).sync();
            // 得到通道，给服务端写数据
            Channel channel = future.channel();
            channel.writeAndFlush("hello");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("itstack-demo-netty client start done. {关注公众号：bugstack虫洞栈，获取源码}");
            channel.closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        //服务类
        Bootstrap bootstrap = new Bootstrap();

        //worker
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            //设置线程池
            bootstrap.group(worker);
            //设置socket工厂、
            bootstrap.channel(NioSocketChannel.class);
            //设置管道
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            });

            ChannelFuture connect = bootstrap.connect("127.0.0.1", 10101);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("请输入：");
                String msg = bufferedReader.readLine();
                connect.channel().writeAndFlush(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
    static class ClientHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("客户端收到消息:"+msg);
        }

    }
}
