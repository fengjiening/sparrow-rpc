package com.fengjiening.sparrow.resolver;

import com.fengjiening.sparrow.enums.ResolverType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: Resolver
 * @Description: 解析器类型
 * @Date: 2022/10/27 19:34
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public interface Resolver {
    public ResolverType resolverType();
}
