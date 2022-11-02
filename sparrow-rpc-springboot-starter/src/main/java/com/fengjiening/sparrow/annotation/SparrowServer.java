package com.fengjiening.sparrow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: SparrowServer
 * @Description: rpc服务注解
 * @Date: 2022/11/2 11:33
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SparrowServer {
    String provider() default "";
    int version();
}
