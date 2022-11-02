package com.fengjiening.sparrow.rules.client;

import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.config.protocol.SparrowProtocol;
import com.fengjiening.sparrow.config.serializer.ProtostuffSerializer;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.manager.CilentManager;
import com.fengjiening.sparrow.pool.ChannelPool;
import com.fengjiening.sparrow.pool.SparrowPoolConfig;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.utill.LogInterceptor;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Rule;

import static com.fengjiening.sparrow.context.SparrowContext.*;

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
        rpcProtocol.setDecoder(new SparrowDecoder());
        rpcProtocol.setEncoder(new SparrowEncoder());
        rpcProtocol.setSerializer(new ProtostuffSerializer());
    }
}
