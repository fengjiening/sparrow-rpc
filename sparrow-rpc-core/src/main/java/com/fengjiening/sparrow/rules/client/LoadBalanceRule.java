package com.fengjiening.sparrow.rules.client;

import com.fengjiening.sparrow.cilent.handle.NettyClientHandle;
import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.manager.SparrowManage;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.utill.LogInterceptor;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Rule;

/**
 * @ClassName: LoadBalanceRule
 * @Description: 加载负载
 * @Date: 2022/10/31 16:52
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Rule(name = "LoadBalanceRule", description = "LoadBalanceRule")
public class LoadBalanceRule extends InstallRule {



    @Action(order = 3)
    @Override
    public void then() throws Exception {
        LogInterceptor.debug("LoadBalanceRule..加载负载");

    }
}
