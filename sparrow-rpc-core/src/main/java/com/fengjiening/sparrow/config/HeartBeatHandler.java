package com.fengjiening.sparrow.config;

import com.fengjiening.sparrow.pool.Connection;
import com.fengjiening.sparrow.utill.LogInterceptor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;

/**
 * <p>
 *  Heart Beat Event Handler
 * </p>
 *
 * @author Jay
 * @date 2022/01/11 13:29
 */
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelHandlerAppender {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            // get protocol code
            Attribute<Byte> attr = ctx.channel().attr(Connection.PROTOCOL);
            if(attr != null){
                Byte code = attr.get();
                if(code != null){
                    LogInterceptor.info("HeartBeatHandler userEventTriggered");
                    //// get protocol from protocol manager
                    //Protocol protocol = ProtocolManager.getProtocol(protocolCode);
                    //// call protocol's heart-beat trigger
                    //HeartBeatTrigger heartBeatTrigger = protocol.getHeartBeatTrigger();
                    //if(heartBeatTrigger != null){
                    //    heartBeatTrigger.heartBeatTriggered(ctx);
                    //}
                }
            }
        }
    }
}
