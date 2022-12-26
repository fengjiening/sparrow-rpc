package com.fengjiening.sparrow.context;

import com.fengjiening.sparrow.cilent.app.TcpClientApp;
import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.config.protocol.SparrowProtocol;
import com.fengjiening.sparrow.config.serializer.ProtostuffSerializer;
import com.fengjiening.sparrow.manager.EngineManager;
import com.fengjiening.sparrow.protocol.Protocol;
import com.fengjiening.sparrow.exception.SparrowCode;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.utill.LogInterceptor;
import com.fengjiening.sparrow.utill.SnowFlake;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @ClassName: SparrowContext
 * @Description: Sparrow全局上下文
 * @Date: 2022/10/26 10:14
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Component
public class SparrowContext implements ApplicationContextAware, InitializingBean{
    public static String contextUuid;
    public static SparrowContext sparrowContext;
    public static SparrowProperties properties;
    public static ApplicationContext applicationContext;
    public static SparrowProtocol rpcProtocol;
    public static String SparrowToken;
    public static EngineManager engineManager=new EngineManager();
    public static TcpClientApp tcpClientApp=new TcpClientApp();
    public static SnowFlake snowFlake = new SnowFlake(0, 0);

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
        //初始化连接池
        initClientPool();
        LogInterceptor.info("服务已启动，contextUuid："+contextUuid);
    }

    private void  initClientPool() {
        long start = System.currentTimeMillis();
        LogInterceptor.info("init Serializable tools");

        long end = System.currentTimeMillis();
        LogInterceptor.info("init Serializable tools finished,cost:"+(end - start) / 1000F+"s");
    }

    private void initRpcProtocol() {
        long start = System.currentTimeMillis();
        LogInterceptor.info("init rpc  protocol");
        rpcProtocol = Optional.ofNullable(rpcProtocol).orElse(new SparrowProtocol());
        rpcProtocol.setDecoder(new SparrowDecoder());
        rpcProtocol.setEncoder(new SparrowEncoder());
        rpcProtocol.setSerializer(new ProtostuffSerializer());
        long end = System.currentTimeMillis();
        LogInterceptor.info("init rpc protocol finished,cost:"+(end - start) / 1000F+"s");
    }

    private void loadProperties() {
        long start = System.currentTimeMillis();
        properties = Optional.ofNullable(properties).orElse(new SparrowProperties());
        LogInterceptor.info("loading sparrow-rpc.properties file");
        ClassLoader classLoader = SparrowProperties.class.getClassLoader();
        try(InputStream stream = classLoader.getResourceAsStream("sparrow-rpc.properties")){
            if(stream!=null){
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


    public static Object getProperties(String key,String def) {
        return properties.get(key,def);
    }
    public static Object getProperties(String key) {
        return properties.get(key);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SparrowContext.applicationContext = applicationContext;
    }
}
