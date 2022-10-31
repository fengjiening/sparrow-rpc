package com.fengjiening.sparrow.config;

import com.fengjiening.sparrow.config.protocol.SparrowProtocol;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.enums.ResolverType;
import com.fengjiening.sparrow.enums.SerializeType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @ClassName: ChannelDecoder
 * @Description: 解码器
 * @Date: 2022/10/26 16:36
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public  class SparrowDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
        // 标记解码起始位置，TCP拆包发生时回退
        in.markReaderIndex();
        // 检查可读数据长度，解决TCP拆包
        if(in.readableBytes() >= SparrowProtocol.HEADER_LENGTH){
            // 报文总长度
            int allLength = in.readInt();
            // 检查协议编号
            byte protocolCode = in.readByte();
            if(protocolCode != SparrowProtocol.protocolCode){
                throw new RuntimeException("decoder error, wrong protocol");
            }
            byte serializeType = in.readByte();
            byte resolverType = in.readByte();
            int id = in.readInt();
            byte[] token = new byte[SparrowProtocol.TOKEN_LENGTH];
            in.readBytes(token);
            byte[] body = new byte[allLength-SparrowProtocol.HEADER_LENGTH];
            in.readBytes(body);
            // 构建完整报文
            RemotingCommand command = RemotingCommand.builder()
                    .length(allLength).id(id)
                    .serializeType( SerializeType.find(serializeType))
                    .resolverType(ResolverType.find(resolverType))
                    .token(token).body(body).build();
            out.add(command);
        }else{
            // 数据长度不足解码首部，发生TCP拆包，回退到解码开始
            in.resetReaderIndex();
        }
    }
}
