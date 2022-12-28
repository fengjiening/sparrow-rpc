package com.fengjiening.sparrow.processor;

import com.fengjiening.sparrow.annotation.SparrowServer;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.enums.Sparrow;
import com.fengjiening.sparrow.exception.SparrowCode;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.proxy.SparrowProxy;
import com.fengjiening.sparrow.server.app.TcpServerApp;
import com.fengjiening.sparrow.utill.ClassScanner;
import com.fengjiening.sparrow.utill.LogInterceptor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import java.util.Map;


/**
 * @ClassName: ServerPostProcessor
 * @Description: TODO
 * @Date: 2022/11/2 11:13
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Data
@Slf4j
public class ServerPostProcessor implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void afterPropertiesSet() throws Exception {
        //Set<String> basePackages = ClassScanner.findBasePackages(applicationContext);
        //basePackages.stream().forEach(pack->registerEntityClazzInfo(pack));
        Map<String, Object> serviceBeans = applicationContext.getBeansWithAnnotation(SparrowServer.class);

        if(serviceBeans.size() == 0){
            return;
        }
        int port = SparrowContext.properties.getInt("sparrow.rpc.client.port",10101);
        TcpServerApp provider = new TcpServerApp(port);
        // 遍历serviceBeans
        for (Map.Entry<String, Object> entry : serviceBeans.entrySet()) {
            Object instance = entry.getValue();
            // 扫描RPCService注解
            if(instance.getClass().isAnnotationPresent(SparrowServer.class)){
                SparrowServer annotation = instance.getClass().getAnnotation(SparrowServer.class);
                Class<?> type =  instance.getClass();
                int version = annotation.version();
                // 封装serviceInfo，缓存在本地服务缓存
                //ServiceInfo serviceInfo = new ServiceInfo(type, version);
                //LocalServiceCache.registerServiceInstance(serviceInfo, new ServiceInstance(type, instance));
            }
        }

        // 启动provider服务
        provider.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    private void registerEntityClazzInfo(String basePackage) {

        String pattern = "classpath*:" + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            if (resources != null) {
                Resource[] var6 = resources;
                int var7 = resources.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    Resource resource = var6[var8];
                    if (resource.isReadable()) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        AnnotationMetadata am = reader.getAnnotationMetadata();
                        String vClassName = reader.getClassMetadata().getClassName();
                        Map<String, Object> attrMap = am.getAnnotationAttributes("com.fengjiening.sparrow.annotation.SparrowServer");
                        Integer version = attrMap == null ? null : (Integer)attrMap.get("version");
                        if (!ObjectUtils.isEmpty(version)) {
                            log.info("ddddd:"+vClassName);
                        }

                    }
                }
            }
        }catch (Exception e){
            throw new SparrowException(new SparrowCode(CommonConstant.SPARROW_TYPE_COMMON_ERROR_CODE),String.format("扫描注解失败，cause ：%s",e.getMessage()));
        }

    }


}
