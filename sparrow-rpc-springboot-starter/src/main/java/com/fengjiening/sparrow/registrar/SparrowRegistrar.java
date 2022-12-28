package com.fengjiening.sparrow.registrar;

import com.fengjiening.sparrow.annotation.EnableSparrow;
import com.fengjiening.sparrow.enums.Sparrow;
import com.fengjiening.sparrow.processor.ClientPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SparrowCilentRegistrar
 * @Description: 注册客户端
 * @Date: 2022/11/2 11:07
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //获取EnableEcho注解的所有属性的value
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableSparrow.class.getName());
        //获取package属性的value
        Sparrow type = (Sparrow)attributes.get("type");
        //使用beanDefinitionRegistry对象将EchoBeanPostProcessor注入至Spring容器中
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ClientPostProcessor.class);
        //给EchoBeanPostProcessor.class中注入packages
        beanDefinitionBuilder.addPropertyValue("type", type);
        beanDefinitionRegistry.registerBeanDefinition(ClientPostProcessor.class.getName(), beanDefinitionBuilder.getBeanDefinition());

    }
}
