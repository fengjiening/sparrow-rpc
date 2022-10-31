package com.fengjiening.sparrow.utill;

/**
 * @ClassName: UUIDUtil
 * @Description: TODO
 * @Date: 2022/10/27 16:47
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
import java.util.UUID;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

public class UUIDUtil {


    /* 计数器。AtomicInteger是java.util.concurrent下的类，JDK的算法工程师会控制好并发问题 */
    private static final AtomicInteger CNT = new AtomicInteger(0);

    /**
     * 静态方法的工具类，应该直接通过类名调用方法，因此申明private构造方法
     */
    private UUIDUtil() {
    }


    /**
     * 生成分布式UUID
     *
     * @return
     */
    public static String getConcurrentUUID(String INSTANCE_NAME) {
        if (null == INSTANCE_NAME) {
            return "The JVM option is null, named 'instance.name'";
        }
        String rs = null;
        StringBuilder sb = new StringBuilder();
        sb.append(INSTANCE_NAME);
        sb.append(System.currentTimeMillis());
        sb.append(CNT.incrementAndGet());
        rs = sb.toString();
        try {
            rs = UUID.nameUUIDFromBytes(rs.getBytes("UTF-8")).toString().replace("-", "");
        } catch (UnsupportedEncodingException e) {
            // TODO 打印error日志，提醒getBytes异常，并打印此时的rs(即是: sb.toString();)
        }
        return rs;
    }
}
