package com.fengjiening.sparrow.utill;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @ClassName: LogInterceptor
 * @Description: TODO
 * @Date: 2022/10/26 11:36
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class LogInterceptor {
    private static final Log log = LogFactory.getLog(LogInterceptor.class);
    public static void info(String message){
        log.info(format(message));
    }
    public static void debug(String message){
        //log.debug(format(message));
        System.err.println(format(message));
    }
    public static void error(String message){
        log.error(format(message));
    }
    private static String format(String message){

        return "##sparrow-rpc## "+message;
    }
}
