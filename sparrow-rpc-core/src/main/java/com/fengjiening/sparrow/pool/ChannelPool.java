package com.fengjiening.sparrow.pool;

import com.fengjiening.sparrow.factory.ChannelFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @ClassName: ChannelPool
 * @Description: 通道池
 * @Date: 2022/11/1 11:19
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class ChannelPool  extends Pool<NettyChannel> {

    public ChannelPool(final GenericObjectPoolConfig poolConfig, final String host, final int port) {
        super(poolConfig,new ChannelFactory(host,port));
    }
    @Override
    public NettyChannel getResource() {
        NettyChannel connection = super.getResource();
        return connection;
    }
    @Override
    public void returnBrokenResource(final NettyChannel resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }
    @Override
    public void returnResource(final NettyChannel resource) {
        if (resource != null) {
            try {
                returnResourceObject(resource);
            } catch (Exception e) {
                returnBrokenResource(resource);
            }
        }
    }

}
