package com.fengjiening.sparrow.cilent.app;

import com.fengjiening.sparrow.config.SparrowThread;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.entity.RpcResponse;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.manager.CilentPoolManager;
import com.fengjiening.sparrow.pool.NettyChannel;
import com.fengjiening.sparrow.pool.SparrowChannelPool;
import com.fengjiening.sparrow.server.SparrowPlatform;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



/**
 * @ClassName: ClientApp
 * @Description: 客户端连接
 * @Date: 2022/10/25 20:28
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class TcpClientApp extends SparrowPlatform {

    @Override
    public void init() {
        //先去找服务地址

        //然后
    }

    @Override
    public void startUp()  {
        //启动
    }

    @Override
    public void shutDown() throws Exception {

    }

    //public  RpcResponse send(RemotingCommand data) throws TimeoutException {
    //    String sync = SparrowContext.getProperties(CommonConstant.PROPERTIES_CONSTANT_RESOLVER, "60").toString();
    //    int time = Integer.valueOf(sync);
    //
    //    SynchronousQueue<RpcResponse> queue = new SynchronousQueue<>();
    //    queueMap.put(data.getOid(),queue);
    //    SparrowChannelPool channelPool = SparrowThread.channelPoolThreadLocal.get();
    //    NettyChannel resource = getResource();
    //    if(resource!=null){
    //        try {
    //            resource.getChannel().writeAndFlush(data);
    //            return queue.poll(time, TimeUnit.SECONDS);
    //        } catch (InterruptedException e) {
    //            TimeoutException timeoutException = new TimeoutException("请求服务端超时，请稍后重试，或调整超时时间sparrow.rpc.timeout配置");
    //            return RpcResponse.builder().exception(timeoutException).build();
    //        }finally {
    //            channelPool.returnResource(resource);
    //        }
    //    }else{
    //        SparrowException timeoutException = new SparrowException(CommonConstant.SPARROW_TYPE_COMMON_ERROR_CODE,"服务不可用");
    //        return RpcResponse.builder().exception(timeoutException).build();
    //    }
    //}
}
