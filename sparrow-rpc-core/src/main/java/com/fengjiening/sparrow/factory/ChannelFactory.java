package com.fengjiening.sparrow.factory;

import com.fengjiening.sparrow.pool.NettyChannel;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @ClassName: ChannelFactory
 * @Description: 通道工程
 * @Date: 2022/11/1 11:22
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class ChannelFactory implements PooledObjectFactory<NettyChannel> {
    private String host;
    private int port;

    public ChannelFactory(String host, int port){
        this.host=host;
        this.port=port;
    }
    @Override
    public void activateObject(PooledObject<NettyChannel> nettyChannel) throws Exception {

    }
    @Override
    public void destroyObject(PooledObject<NettyChannel> nettyChannel) throws Exception{

    }
    @Override
    public PooledObject<NettyChannel> makeObject() throws Exception {
        NettyChannel conn=new NettyChannel(host,port);
        return new DefaultPooledObject<NettyChannel>(conn);
    }
    @Override
    public void passivateObject(PooledObject<NettyChannel> nettyChannel) throws Exception {
        // TODO maybe should select db 0? Not sure right now.
    }
    @Override
    public boolean validateObject(PooledObject<NettyChannel> nettyChannel) {
        return true;
    }
}
