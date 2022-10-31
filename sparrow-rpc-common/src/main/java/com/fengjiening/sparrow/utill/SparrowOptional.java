package com.fengjiening.sparrow.utill;

import com.fengjiening.sparrow.exception.SparrowCode;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.function.ExceptionFunction;
import com.fengjiening.sparrow.function.IfElseFunction;

/**
 * @ClassName: SparrowOptional
 * @Description: 全局简化类
 * @Date: 2022/10/28 15:14
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowOptional {

    /**
     * 异常抛出
     * @param falg
     * @return
     */
    public static ExceptionFunction ture(boolean falg){
        return (code ,errorMessage) -> {
            if (falg){
                throw new SparrowException(new SparrowCode(code),errorMessage);
            }
        };
    }
    public static void throwsException(String code ,String errorMessage){
        throw new SparrowException(new SparrowCode(code),errorMessage);
    }

    public static IfElseFunction condition(boolean falg){
        return ( function,function1) -> {
            if (falg){
                //表达式成立 ，执行第一个方法
                function.apply(null);
            }else{
                //表达式不成立 ，执行第二个方法
                function1.apply(null);
            }
        };
    }
}
