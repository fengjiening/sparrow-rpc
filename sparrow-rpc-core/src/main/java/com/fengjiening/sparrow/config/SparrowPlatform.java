package com.fengjiening.sparrow.config;

import com.fengjiening.sparrow.manager.ConnectionManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;

import java.util.List;

/**
 * @ClassName: SparrowPlatform
 * @Description: TODO
 * @Date: 2022/10/31 14:10
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public abstract class SparrowPlatform {


    public abstract void setUp();
    public abstract void startUp();
    public abstract void shutDown();

}
