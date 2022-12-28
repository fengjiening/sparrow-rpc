package com.fengjiening.sparrow.context;

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

    public  String get(String name, String defaultValue){
        String value = getProperty(name);
        return value != null ? value : defaultValue;
    }

    public  String getString(String name){
        return get(name,null);
    }

    public  int getInt(String name, int defaultValue){
        String value = getString(name);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public  boolean getBoolean(String name, boolean defaultValue){
        String property = getString(name);
        return property != null ? Boolean.parseBoolean(property) : defaultValue;
    }
}
