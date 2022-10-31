package com.fengjiening.sparrow.manager;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: ManageSparrow
 * @Description: TODO
 * @Date: 2022/10/28 10:33
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowManage {
    public static ThreadLocal<ChannelHandlerContext> channelThreadLocal = new ThreadLocal<>();
}
