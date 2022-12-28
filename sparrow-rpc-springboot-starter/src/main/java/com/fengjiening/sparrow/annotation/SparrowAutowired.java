package com.fengjiening.sparrow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *
 * </p>
 *
 * @author Jay
 * @date 2022/03/22 13:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SparrowAutowired {
    String provider() default "";
    int version() default 0;;
}
