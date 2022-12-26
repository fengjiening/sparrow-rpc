package com.fengjiening.sparrow.config;

import com.fengjiening.sparrow.config.protocol.SparrowProtocol;
import com.fengjiening.sparrow.config.vo.ChannelData;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName: Encoder
 * @Description: TODO
 * @Date: 2022/10/26 16:36
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public  class SparrowEncoder extends MessageToByteEncoder<RemotingCommand> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RemotingCommand rpcBufferVo, ByteBuf byteBuf) throws Exception {
        //报文头
        byteBuf.writeInt(rpcBufferVo.getLength());
        //协议号 1
        byteBuf.writeByte(SparrowProtocol.protocolCode);
        //序列类型 1
        byteBuf.writeByte( rpcBufferVo.getSerializeType().getValue());
        //解析器类型 1
        byteBuf.writeByte( rpcBufferVo.getResolverType().getType());
        //请求id 4
        byteBuf.writeBytes(rpcBufferVo.getId());
        //token 32
        byteBuf.writeBytes(rpcBufferVo.getToken());
        //报文体
        byteBuf.writeBytes(rpcBufferVo.getBody());
    }
}
