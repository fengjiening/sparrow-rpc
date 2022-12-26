package com.fengjiening.sparrow.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: SparrowServer
 * @Description: rpc服务注解
 * @Date: 2022/11/2 11:33
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SparrowServer {
    String provider() default "";
    int version()  default 1;
}
