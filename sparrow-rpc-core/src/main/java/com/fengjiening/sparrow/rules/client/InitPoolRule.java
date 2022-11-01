package com.fengjiening.sparrow.rules.client;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.manager.CilentManager;
import com.fengjiening.sparrow.manager.SparrowManage;
import com.fengjiening.sparrow.pool.ChannelPool;
import com.fengjiening.sparrow.pool.NettyChannel;
import com.fengjiening.sparrow.pool.SparrowPoolConfig;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.rules.base.SparrowRule;
import com.fengjiening.sparrow.utill.LogInterceptor;
import io.netty.bootstrap.AbstractBootstrap;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Rule;

/**
 * @ClassName: InitPoolRule
 * @Description: 初始化连接池
 * @Date: 2022/10/31 16:30
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Rule(name = "InitPoolRule", description = "InitPoolRule")
public class InitPoolRule extends InstallRule {


    @Action(order = 1)
    @Override
    public void then() throws Exception {
        LogInterceptor.debug("InitPoolRule..初始化连接池");
        String host="127.0.0.1";
        int port=10101;

        SparrowPoolConfig config=new SparrowPoolConfig();
        config.setMaxIdle(10);//最大活跃数
        config.setMinIdle(1);//最小活跃数
        config.setMaxTotal(100);//最大总数
        //创建资源池
        CilentManager.initChannelPool(new ChannelPool(config,host,port));
        //获取连接
       // NettyChannel nettyChannel=channelPool.getResource();


    }
}
