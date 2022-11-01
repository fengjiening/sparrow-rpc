package com.fengjiening.sparrow.manager;

import com.fengjiening.sparrow.pool.ChannelPool;
import com.fengjiening.sparrow.pool.NettyChannel;
import lombok.AllArgsConstructor;

/**
 * @ClassName: CilentManager
 * @Description: 客户端管理
 * @Date: 2022/10/28 20:57
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class CilentManager implements SparrowManage{
    private static ChannelPool channelPool;

    public static NettyChannel getResource(){
        return channelPool.getResource();
    }
    public static void initChannelPool(ChannelPool pool){
        channelPool=pool;
    }
    public static void  close(){
        NettyChannel resource = getResource();
        resource.shutdown();
        channelPool.destroy();
    }

    public static void send(Object data){
        NettyChannel resource = getResource();
        if(resource!=null){
            resource.getChannel().writeAndFlush(data);
            returnResource(resource);
        }
    }

    private static void returnResource(NettyChannel resource){
        channelPool.returnResource(resource);
    }


}
