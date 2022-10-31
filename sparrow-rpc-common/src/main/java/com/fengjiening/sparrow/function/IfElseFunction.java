package com.fengjiening.sparrow.function;

import java.util.function.Function;

/**
 * @ClassName: IfElseFunction
 * @Description: ifelse模板
 * @Date: 2022/10/28 15:13
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@FunctionalInterface
public interface IfElseFunction {

    void exec(Function callback1, Function callback2 );
}
