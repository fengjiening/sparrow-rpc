package com.fengjiening.sparrow.resolver;

import com.fengjiening.sparrow.config.SparrowThread;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.manager.SparrowManage;
import com.fengjiening.sparrow.config.SparrowThreadPool;
import com.fengjiening.sparrow.utill.SparrowOptional;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executor;

/**
 * @ClassName: Resolver
 * @Description: 解析器类型
 * @Date: 2022/10/27 19:34
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public abstract class  SparrowResolver implements Resolver{
    private RemotingCommand command;

    public  void doExecute (ChannelHandlerContext channelHandlerContext){
        String properties = SparrowContext.getProperties(CommonConstant.PROPERTIES_CONSTANT_RESOLVER,"sync").toString();
        SparrowOptional.condition("async".equals(properties)).exec((s)->{
            asyncExecute(channelHandlerContext);
            return s;
        },(s)->{
            execute(channelHandlerContext);
            return s;
        });
    };

    public  void asyncExecute (ChannelHandlerContext channelHandlerContext){
        //开启线程池
        Executor pool = SparrowThreadPool.pool();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                SparrowThread.channelThreadLocal.set(channelHandlerContext);
                execute(SparrowThread.channelThreadLocal.get());
                SparrowThread.channelThreadLocal.remove();
            }
        });
    };
    public void flushData(RemotingCommand command){
        this.command=command;
    }
    public RemotingCommand commandData(){
        return this.command;
    }
    public abstract void execute (ChannelHandlerContext channelHandlerContext);
}
