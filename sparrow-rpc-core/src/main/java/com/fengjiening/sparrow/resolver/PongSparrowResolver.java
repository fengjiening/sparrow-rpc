package com.fengjiening.sparrow.resolver;

import com.fengjiening.sparrow.enums.ResolverType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: PongSparrowResolver
 * @Description: Pong 解析器
 * @Date: 2022/10/27 20:03
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class PongSparrowResolver extends SparrowResolver{
    @Override
    public ResolverType resolverType() {
        return ResolverType.PONG;
    }

    @Override
    public void execute(ChannelHandlerContext channelHandlerContext) {

    }
}
