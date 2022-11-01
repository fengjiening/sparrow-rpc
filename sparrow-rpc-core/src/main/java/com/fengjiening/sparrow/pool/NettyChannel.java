package com.fengjiening.sparrow.pool;

import com.fengjiening.sparrow.cilent.handle.NettyClientHandle;
import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.utill.SparrowOptional;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName: NettyChannel
 * @Description: 客户端连接对象类
 * @Date: 2022/11/1 11:07
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class NettyChannel {
    private String host;
    private int port;
    private Bootstrap  bootstrap ;
    private EventLoopGroup  worker;
    public NettyChannel(String host, int port){
            this.host=host;
            this.port=port;
            connect();

    }
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void connect() {
        bootstrap=new Bootstrap();
        worker= new NioEventLoopGroup();
        // 设置线程池
        bootstrap.group(worker);
        NettyClientHandle nettyClientHandler= new NettyClientHandle();
        try {
            // 设置socket工厂
            bootstrap.channel(NioSocketChannel.class);
            // 设置管道
            bootstrap.handler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    // 添加用于处理粘包和拆包问题的处理器
                    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                    channel.pipeline().addLast(new LengthFieldPrepender(4));
                    channel.pipeline().addLast( new SparrowEncoder());
                    channel.pipeline().addLast(new SparrowDecoder());
                    channel.pipeline().addLast(nettyClientHandler);
                }
            });
            //ChannelFuture future = boot.connect(host, port).sync();
            //future.channel().closeFuture().sync();
            channel = bootstrap.connect(host, port).sync().channel();
        }catch (Exception es){
            SparrowOptional.throwsException(CommonConstant.SPARROW_TYPE_CONNECT_FAILD_ERROR_CODE,"无法连接服务端");
        }finally {
            worker.shutdownGracefully();
        }
    }
    public void shutdown(){
        if(!ObjectUtils.isEmpty(worker)){
            worker.shutdownGracefully();
        }
    }
}
