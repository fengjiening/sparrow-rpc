package com.fengjiening.sparrow.resolver;

import com.fengjiening.sparrow.enums.ResolverType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: ResponseSparrowResolver
 * @Description: 响应解析器
 * @Date: 2022/10/27 20:03
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class ResponseSparrowResolver extends SparrowResolver{
    @Override
    public ResolverType resolverType() {
        return ResolverType.RESPONSE;
    }

    @Override
    public void execute(ChannelHandlerContext channelHandlerContext) {

    }
}
