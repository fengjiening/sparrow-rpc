package com.fengjiening.sparrow.context;

import com.fengjiening.sparrow.enums.SparrowCode;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.utill.LogInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @ClassName: SparrowContext
 * @Description: Sparrow全局上下文
 * @Date: 2022/10/26 10:14
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowContext implements ApplicationContextAware, InitializingBean{
    protected static SparrowContext sparrowContext;
    protected static SparrowProperties properties;
    protected static ApplicationContext applicationContext;
    protected static String SparrowToken;

    public static SparrowContext getInstance() {
        return sparrowContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadProperties();

    }

    private void loadProperties() {
        properties = Optional.ofNullable(properties).orElse(new SparrowProperties());
        LogInterceptor.info("loading sparrow-rpc.properties file");
        ClassLoader classLoader = SparrowProperties.class.getClassLoader();
        try(InputStream stream = classLoader.getResourceAsStream("sparrow-rpc.properties")){
            properties.load(stream);
        }catch (IOException e){
            LogInterceptor.error("sparrow-rpc.properties not find file");
            throw new SparrowException(new SparrowCode(CommonConstant.FILE_NOT_FIND_CODE),"sparrow-rpc.properties not find",e.getCause());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SparrowContext.applicationContext = applicationContext;
    }
}
