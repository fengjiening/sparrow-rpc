package com.fengjiening.sparrow.annotation;

import com.fengjiening.sparrow.enums.Sparrow;
import com.fengjiening.sparrow.registrar.SparrowCilentRegistrar;
import com.fengjiening.sparrow.registrar.SparrowServerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName: EnableSparrow
 * @Description:
 * @Date: 2022/11/2 11:05
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SparrowCilentRegistrar.class,SparrowServerRegistrar.class})
public @interface EnableSparrow {
    Sparrow type () default Sparrow.All;
}
