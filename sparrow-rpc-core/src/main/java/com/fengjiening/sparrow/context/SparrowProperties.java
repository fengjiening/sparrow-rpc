package com.fengjiening.sparrow.context;

import java.util.Properties;

/**
 * @ClassName: SparrowProperties
 * @Description: TODO
 * @Date: 2022/10/26 10:43
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowProperties extends Properties {

    public String get(String name, String defaultValue){
        String value = getProperty(name);
        return value != null ? value : defaultValue;
    }
}
