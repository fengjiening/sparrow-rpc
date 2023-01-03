package com.fengjiening.sparrow.utill;

import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.exception.SparrowCode;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.utill.LogInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * @ClassName: SparrowProperties
 * @Description: TODO
 * @Date: 2022/10/26 10:43
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class PropertiesUtil extends Properties {
    private static Properties properties;

    static{
        long start = System.currentTimeMillis();
        LogInterceptor.info("loading sparrow-rpc.properties file");
        ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
        try(InputStream stream = classLoader.getResourceAsStream("sparrow-rpc.properties")){
            if(stream!=null){
                properties = new Properties();
                properties.load(stream);
            }else{
                throw new IOException();
            }
        }catch (IOException e){
            LogInterceptor.error("sparrow-rpc.properties not find file");
            throw new SparrowException(new SparrowCode(CommonConstant.FILE_NOT_FIND_CODE),"sparrow-rpc.properties not find",e.getCause());
        }
        long end = System.currentTimeMillis();
        LogInterceptor.info("loading sparrow-rpc.properties file finished,cost:"+(end - start) / 1000F+"s");
    }

    public static String get(String name, String defaultValue){
        String value = properties.getProperty(name);
        return value != null ? value : defaultValue;
    }

    public static String get(String name){
        return properties.getProperty(name);
    }

    public static int getInt(String name, int defaultValue){
        String value = get(name);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public static boolean getBoolean(String name, boolean defaultValue){
        String property = get(name);
        return property != null ? Boolean.parseBoolean(property) : defaultValue;
    }

    public static Properties getProperties(){
        return properties;
    }
}
