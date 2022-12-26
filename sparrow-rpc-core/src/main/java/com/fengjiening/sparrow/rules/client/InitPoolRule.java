package com.fengjiening.sparrow.rules.client;

import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.manager.CilentPoolManager;
import com.fengjiening.sparrow.pool.SparrowChannelPool;
import com.fengjiening.sparrow.pool.SparrowPoolConfig;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.utill.LogInterceptor;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Rule;

import java.net.Inet4Address;

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
        String serverHost = Inet4Address.getLocalHost().getHostAddress();
        int port = Integer.valueOf(SparrowContext.getProperties("sparrow.rpc.client.port", "10101").toString());
        int maxidle = Integer.valueOf(SparrowContext.getProperties("sparrow.rpc.client.maxidle", "10").toString());
        int minidle = Integer.valueOf(SparrowContext.getProperties("sparrow.rpc.client.minidle", "1").toString());
        int maxtotal = Integer.valueOf(SparrowContext.getProperties("sparrow.rpc.client.maxtotal", "100").toString());
        SparrowPoolConfig config=new SparrowPoolConfig();
        config.setMaxIdle(maxidle);//最大活跃数
        config.setMinIdle(minidle);//最小活跃数
        config.setMaxTotal(maxtotal);//最大总数
        //创建资源池
    }
}
