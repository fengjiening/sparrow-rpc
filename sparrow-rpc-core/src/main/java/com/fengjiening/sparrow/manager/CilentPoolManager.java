package com.fengjiening.sparrow.manager;

import com.fengjiening.sparrow.config.SparrowThread;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.entity.RpcResponse;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.pool.SparrowChannelPool;
import com.fengjiening.sparrow.pool.NettyChannel;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName: CilentPoolManager
 * @Description: 连接池管理
 * @Date: 2022/10/28 20:57
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class CilentPoolManager implements SparrowManage{
   private final ConcurrentHashMap<String, FutureTask<SparrowChannelPool>> connPoolTasks = new ConcurrentHashMap<>(256);

}
