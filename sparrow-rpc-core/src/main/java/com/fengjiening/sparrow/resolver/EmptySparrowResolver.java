package com.fengjiening.sparrow.resolver;

import com.fengjiening.sparrow.enums.ResolverType;
import com.fengjiening.sparrow.manager.SparrowManage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: EmptySparrowResolver
 * @Description:  空解析器
 * @Date: 2022/10/27 20:03
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class EmptySparrowResolver extends SparrowResolver{
    @Override
    public ResolverType resolverType() {
        return ResolverType.EMPTY;
    }

    @Override
    public void execute(ChannelHandlerContext channelHandlerContext) {
        SparrowManage.channelThreadLocal.set(channelHandlerContext);
    }
}
