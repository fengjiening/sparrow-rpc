package com.fengjiening.sparrow.manager;

import com.fengjiening.sparrow.config.SparrowThread;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.entity.RpcResponse;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.pool.ChannelPool;
import com.fengjiening.sparrow.pool.NettyChannel;
import com.fengjiening.sparrow.serializer.SparrowSerializer;

import java.util.concurrent.*;

/**
 * @ClassName: CilentManager
 * @Description: 客户端管理
 * @Date: 2022/10/28 20:57
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class CilentManager implements SparrowManage{
   public static ConcurrentHashMap<String, SynchronousQueue<RpcResponse>> queueMap = new ConcurrentHashMap<>();
    public static NettyChannel getResource(){
        ChannelPool channelPool = SparrowThread.channelPoolThreadLocal.get();
        return channelPool.getResource();
    }
    public static void initChannelPool(ChannelPool pool){
        SparrowThread.channelPoolThreadLocal.set(pool);
    }
    public static void  close(){
        ChannelPool channelPool = SparrowThread.channelPoolThreadLocal.get();
        NettyChannel resource = getResource();
        resource.shutdown();
        SparrowThread.channelPoolThreadLocal.remove();
        channelPool.destroy();

    }

    public static RpcResponse send(RemotingCommand data) throws TimeoutException {
        String sync = SparrowContext.getProperties(CommonConstant.PROPERTIES_CONSTANT_RESOLVER, "60").toString();
        int time = Integer.valueOf(sync);

        SynchronousQueue<RpcResponse> queue = new SynchronousQueue<>();
        queueMap.put(data.getId(),queue);
        ChannelPool channelPool = SparrowThread.channelPoolThreadLocal.get();
        NettyChannel resource = getResource();
        if(resource!=null){
            try {
                resource.getChannel().writeAndFlush(data);
                return queue.poll(time, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                TimeoutException timeoutException = new TimeoutException("请求服务端超时，请稍后重试，或调整超时时间sparrow.rpc.timeout配置");
                return RpcResponse.builder().exception(timeoutException).build();
            }finally {
                channelPool.returnResource(resource);
            }
        }else{
            SparrowException timeoutException = new SparrowException(CommonConstant.SPARROW_TYPE_COMMON_ERROR_CODE,"服务不可用");
            return RpcResponse.builder().exception(timeoutException).build();
        }
    }

}
