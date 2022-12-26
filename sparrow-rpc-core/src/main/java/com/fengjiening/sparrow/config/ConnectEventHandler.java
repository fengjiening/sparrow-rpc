package com.fengjiening.sparrow.config;

import com.fengjiening.sparrow.enums.ConnectEvent;
import com.fengjiening.sparrow.manager.ConnectionManager;
import com.fengjiening.sparrow.pool.Connection;
import com.fengjiening.sparrow.utill.LogInterceptor;
import io.netty.channel.*;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * @ClassName: ConnectEventHandler
 * @Description: TODO
 * @Date: 2022/11/4 16:12
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ConnectEventHandler extends ChannelHandlerAppender {
    private ConnectionManager connectionManager;

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        Channel channel = ctx.channel();
        Connection connection = channel.attr(Connection.CONNECTION).get();
        if(connection != null){
            connection.onClose();
        }
        super.close(ctx, promise);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // get connection bind to this channel
        Attribute<Connection> attr = ctx.channel().attr(Connection.CONNECTION);
        if(attr != null){
            Connection connection = attr.get();
            // check if connection manager present, server-side may be absent
            if(connection != null){
                connectionManager.removeConnectionPool(connection.getPoolKey());
                connection.onClose();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(log.isDebugEnabled()){
            log.debug("channel error: ", cause);
        }
        this.userEventTriggered(ctx, ConnectEvent.EXCEPTION);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof ConnectEvent){
            ConnectEvent event = (ConnectEvent)evt;
            Channel channel = ctx.channel();
            if(channel == null){
                return;
            }
            Attribute<Connection> attr = ctx.channel().attr(Connection.CONNECTION);
            Connection connection = attr.get();
            if(connection == null){
                return;
            }
            switch(event){
                case CONNECT:
                    LogInterceptor.info("ConnectEventHandler CONNECT..");
                case EXCEPTION:
                    LogInterceptor.info("ConnectEventHandler EXCEPTION..");
                default:break;
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
