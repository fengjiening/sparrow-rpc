package com.fengjiening.sparrow.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @ClassName: SparrowServer
 * @Description: rpc服务注解
 * @Date: 2022/11/2 11:33
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface SparrowServer {


    /**
     * 版本号
     * @return int
     */
    int version() default 0;
}
