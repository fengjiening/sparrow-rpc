package com.fengjiening.sparrow.cilent.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: NettyClientHandle
 * @Description: TODO
 * @Date: 2022/10/25 20:39
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class NettyClientHandle extends SimpleChannelInboundHandler<String> {

    /**
     * 接收客户端发送的数据
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("接收到的客户端发送的消息：" + msg);
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
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
