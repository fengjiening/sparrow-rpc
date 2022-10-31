package com.fengjiening.sparrow.resolver;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.enums.ResolverType;
import com.fengjiening.sparrow.manager.SparrowManage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: RequestSparrowResolver
 * @Description: 请求解析器
 * @Date: 2022/10/27 20:03
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class RequestSparrowResolver extends SparrowResolver{
    @Override
    public ResolverType resolverType() {
        return ResolverType.REQUEST;
    }

    @Override
    public void execute(ChannelHandlerContext channelHandlerContext) {
        RemotingCommand remotingCommand = super.commandData();
        channelHandlerContext.writeAndFlush(remotingCommand);
    }
}
