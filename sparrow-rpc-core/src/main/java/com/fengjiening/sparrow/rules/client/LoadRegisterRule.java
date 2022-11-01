package com.fengjiening.sparrow.rules.client;

import com.fengjiening.sparrow.manager.SparrowManage;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.utill.LogInterceptor;
import io.netty.bootstrap.AbstractBootstrap;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Rule;

/**
 * @ClassName: LoadRegisterRule
 * @Description: 加载注册中心
 * @Date: 2022/10/31 16:30
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Rule(name = "LoadRegisterRule", description = "LoadRegisterRule")
public class LoadRegisterRule extends InstallRule {
    @Action(order = 3)
    @Override
    public void then() throws Exception {
        LogInterceptor.debug("LoadRegisterRule..加载注册中心");
    }
}
