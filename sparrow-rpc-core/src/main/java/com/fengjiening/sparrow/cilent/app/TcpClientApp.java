package com.fengjiening.sparrow.cilent.app;

import com.fengjiening.sparrow.cilent.handle.NettyClientHandle;
import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.manager.CilentManager;
import com.fengjiening.sparrow.manager.EngineManager;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.server.SparrowPlatform;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;
import org.jeasy.rules.api.RulesEngine;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;


/**
 * @ClassName: ClientApp
 * @Description: 客户端连接
 * @Date: 2022/10/25 20:28
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class TcpClientApp implements SparrowPlatform {

    @Override
    public void startUp()  {
        List<InstallRule> installRules = EngineManager.installCilentApp();
        new EngineManager().execute(installRules);
    }

    @Override
    public void shutDown() throws Exception {
        CilentManager.close();
    }
}
