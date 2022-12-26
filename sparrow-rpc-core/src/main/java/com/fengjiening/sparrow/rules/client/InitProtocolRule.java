package com.fengjiening.sparrow.rules.client;

import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.config.serializer.ProtostuffSerializer;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.utill.LogInterceptor;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Rule;

/**
 * @ClassName: InitPoolRule
 * @Description: 初始化协议
 * @Date: 2022/10/31 16:30
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Rule(name = "InitProtocolRule", description = "InitProtocolRule")
public class InitProtocolRule extends InstallRule {


    @Action(order = 2)
    @Override
    public void then() throws Exception {
        LogInterceptor.debug("InitPoolRule..初始化连接池");
        SparrowContext.rpcProtocol.setDecoder(new SparrowDecoder());
        SparrowContext.rpcProtocol.setEncoder(new SparrowEncoder());
        SparrowContext.rpcProtocol.setSerializer(new ProtostuffSerializer());
    }
}
