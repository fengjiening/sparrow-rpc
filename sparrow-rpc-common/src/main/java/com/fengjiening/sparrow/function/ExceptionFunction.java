package com.fengjiening.sparrow.function;

/**
 * @ClassName: d
 * @Description: 异常模板
 * @Date: 2022/10/28 15:13
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@FunctionalInterface
public interface ExceptionFunction {
    /**
     * 抛出异常信息
     *@param code 异常code
     * @param message 异常信息
     * @return void
     **/
    void throwsException(String code ,String message);
}
