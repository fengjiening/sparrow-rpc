package com.fengjiening.sparrow.processor;

import com.fengjiening.sparrow.annotation.SparrowServer;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.enums.Sparrow;
import com.fengjiening.sparrow.exception.SparrowCode;
import com.fengjiening.sparrow.exception.SparrowException;
import com.fengjiening.sparrow.proxy.SparrowProxy;
import com.fengjiening.sparrow.server.LocalServiceCache;
import com.fengjiening.sparrow.server.ServiceInfo;
import com.fengjiening.sparrow.server.ServiceInstance;
import com.fengjiening.sparrow.server.app.TcpServerApp;
import com.fengjiening.sparrow.utill.ClassScanner;
import com.fengjiening.sparrow.utill.LogInterceptor;
import com.fengjiening.sparrow.utill.PropertiesUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import java.util.Map;
import java.util.Set;
import java.util.logging.Filter;


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
        Set<String> basePackages = ClassScanner.findBasePackages(applicationContext);
        basePackages.stream().forEach(pack->registerEntityClazzInfo(pack));
        int port = PropertiesUtil.getInt("sparrow.rpc.client.port",10101);
        TcpServerApp provider = new TcpServerApp(port);
        // 启动provider服务
        provider.startUp();
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
                        if(!CollectionUtils.isEmpty(attrMap)){
                            // 查找所有beanMap接口的实现类
                            Class clazz = Class.forName(vClassName);
                            Integer version = attrMap == null ? null : (Integer)attrMap.get("version");
                            //指定扫描的包名
                            Reflections reflections = new Reflections(basePackage);

                            //Filter是个接口，获取在指定包扫描的目录所有的实现类
                            Set<Class>classes = reflections.getSubTypesOf(clazz);
                            if(!CollectionUtils.isEmpty(classes)){
                                Class<?> type =classes.iterator().next();
                                // 封装serviceInfo，缓存在本地服务缓存
                                ServiceInfo serviceInfo = new ServiceInfo(clazz, type,version);
                                LocalServiceCache.registerServiceInstance(serviceInfo, new ServiceInstance(type, type.newInstance()));

                            }
                            //Map<String, Object> beanMap = applicationContext.getBeansOfType(clazz);
                            //if(!CollectionUtils.isEmpty(beanMap)){
                            //    Set<String> key = beanMap.keySet();
                            //    Class<?> type =beanMap.get(key.iterator().next()).getClass();
                            //    // 封装serviceInfo，缓存在本地服务缓存
                            //    ServiceInfo serviceInfo = new ServiceInfo(type, version);
                            //    LocalServiceCache.registerServiceInstance(serviceInfo, new ServiceInstance(type, type.newInstance()));
                            //
                            //}
                        }
                    }
                }
            }
        }catch (Exception e){
            throw new SparrowException(new SparrowCode(CommonConstant.SPARROW_TYPE_COMMON_ERROR_CODE),String.format("扫描注解失败，cause ：%s",e.getMessage()));
        }

    }


}
