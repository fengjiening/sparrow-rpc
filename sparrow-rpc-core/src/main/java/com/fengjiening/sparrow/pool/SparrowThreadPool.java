package com.fengjiening.sparrow.pool;

import com.fengjiening.sparrow.utill.LogInterceptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName: SparrowThreadPool
 * @Description: 线程池
 * @Date: 2022/10/28 11:10
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowThreadPool {
    private static Executor executor;

    public static void  initPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        //此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors()+1;
        LogInterceptor.debug("处理器的虚拟机的最大数量->"+core);
        executor.setCorePoolSize(core);
        // 设置最大线程数
        executor.setMaxPoolSize(core+5);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("sparrow-");

        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        //executor.setWaitForTasksToCompleteOnShutdown(true);
    }

    public static Executor pool(){
        if(Objects.isNull(executor)){
            initPool();
        }
        return executor;
    }
}
