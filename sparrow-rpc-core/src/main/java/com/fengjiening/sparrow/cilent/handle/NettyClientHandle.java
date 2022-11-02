package com.fengjiening.sparrow.cilent.handle;

import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.config.protocol.SparrowProtocol;
import com.fengjiening.sparrow.config.serializer.ProtostuffSerializer;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.enums.SerializeType;
import com.fengjiening.sparrow.factory.SparrowResolverFactory;
import com.fengjiening.sparrow.resolver.SparrowResolver;
import com.fengjiening.sparrow.result.Result;
import com.fengjiening.sparrow.serializer.SparrowSerializer;
import com.fengjiening.sparrow.utill.LogInterceptor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName: NettyClientHandle
 * @Description: NettyClientHandle
 * @Date: 2022/10/25 20:39
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class NettyClientHandle extends SimpleChannelInboundHandler<RemotingCommand> {
    private static SparrowResolverFactory sparrowResolverFactory=SparrowResolverFactory.getInstance();

    /**
     * 接收客户端发送的数据
     * rpc请求接受
     * @param channelHandlerContext
     * @param command
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, RemotingCommand command) throws Exception {
        LogInterceptor.debug("接收到的服务端发送的消息：" + command.toString()+"thread name - "+Thread.currentThread().getName());
        SparrowResolver resolver = sparrowResolverFactory.getResolver(command);
        resolver.doExecute(channelHandlerContext);

    }

    /**
     * 客户端接入
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogInterceptor.info("服务端接入");
        super.channelActive(ctx);
        Result<Object> ok = Result.ok("hahah");

        SparrowDecoder decoder = SparrowContext.rpcProtocol.getDecoder();
        SparrowEncoder encoder = SparrowContext.rpcProtocol.getEncoder();
        SparrowSerializer serializer = SparrowContext.rpcProtocol.getSerializer();
        byte[] serialize = serializer.serialize(ok);


        RemotingCommand command = RemotingCommand.builder()
                .length(serialize.length+ SparrowProtocol.HEADER_LENGTH).id(999)
                .serializeType(serializer.type())
                .token("1ca2f21bdc0f3ab383cd21518ff357ec".getBytes()).body(serialize).build();
        ctx.writeAndFlush(command);
    }

    /**
     * 客户端断开（相当于3的disConnected）
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogInterceptor.info("服务端断开");
        super.channelInactive(ctx);
    }
}
