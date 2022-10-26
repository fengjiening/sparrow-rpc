package com.fengjiening.sparrow.server.handle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: NettyServerHandle
 * @Description: TODO
 * @Date: 2022/10/25 20:42
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class NettyUpdServerHandle extends SimpleChannelInboundHandler<String> {

    /**
     * 接收客户端发送的数据
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("messageReceived: " + msg);
        // 得到回写到客户端的channel
        Channel channel = channelHandlerContext.channel();
        channel.write("服务端接受到的消息是: " + msg + "from server");
        // 这里必须flush，否则客户端收不到消息（不会立刻发送，如果不进行flush）
        channel.flush();
        /*
        // 可以直接调用该方法，该方法相当于write+flush
        channel.writeAndFlush(msg);
        // 也可以用下面的方法，都是同一个方法
        channelHandlerContext.writeAndFlush(msg);
        */
    }

    /**
     * 客户端接入
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
    }

    /**
     * 客户端断开（相当于3的disConnected）
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
