package com.fengjiening.sparrow.config;

import com.fengjiening.sparrow.pool.SparrowChannelPool;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName: ManageSparrow
 * @Description: TODO
 * @Date: 2022/10/28 10:33
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowThread {
    public static ThreadLocal<ChannelHandlerContext> channelThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<SparrowChannelPool> channelPoolThreadLocal = new ThreadLocal<>();
}
