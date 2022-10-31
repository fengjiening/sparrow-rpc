package com.fengjiening.sparrow.context;

import com.fengjiening.sparrow.manager.EngineManager;
import com.fengjiening.sparrow.protocol.Protocol;
import com.fengjiening.sparrow.exception.SparrowCode;
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
import java.util.UUID;

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
    protected static Protocol rpcProtocol;
    protected static String SparrowToken;
    protected static String contextUuid;
    protected static EngineManager engineManager=new EngineManager();

    public static SparrowContext getInstance() {
        return sparrowContext;
    }
    public static EngineManager getEngineManager() {
        return engineManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        contextUuid=UUID.randomUUID().toString();
        //加载配置
        loadProperties();
        //初始化rpc协议
        initRpcProtocol();
        //初始化序列器。
        initSerializable();
        LogInterceptor.info("服务已启动，contextUuid："+contextUuid);
    }



    private void  initSerializable() {
        long start = System.currentTimeMillis();
        LogInterceptor.info("init Serializable tools");

        long end = System.currentTimeMillis();
        LogInterceptor.info("init Serializable tools finished,cost:"+(end - start) / 1000F+"s");
    }

    private void initRpcProtocol() {
        long start = System.currentTimeMillis();
        LogInterceptor.info("init rpc  protocol");
        long end = System.currentTimeMillis();
        LogInterceptor.info("init rpc protocol finished,cost:"+(end - start) / 1000F+"s");
    }

    private void loadProperties() {
        long start = System.currentTimeMillis();
        properties = Optional.ofNullable(properties).orElse(new SparrowProperties());
        LogInterceptor.info("loading sparrow-rpc.properties file");
        ClassLoader classLoader = SparrowProperties.class.getClassLoader();
        try(InputStream stream = classLoader.getResourceAsStream("sparrow-rpc.properties")){
            properties.load(stream);
        }catch (IOException e){
            LogInterceptor.error("sparrow-rpc.properties not find file");
            throw new SparrowException(new SparrowCode(CommonConstant.FILE_NOT_FIND_CODE),"sparrow-rpc.properties not find",e.getCause());
        }
        long end = System.currentTimeMillis();
        LogInterceptor.info("loading sparrow-rpc.properties file finished,cost:"+(end - start) / 1000F+"s");
    }


    public static Object getProperties(String key,String def) {
        //todo
        return def;
        //return properties.get(key,def);
    }
    public static Object getProperties(String key) {
        return properties.get(key);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SparrowContext.applicationContext = applicationContext;
    }
}
