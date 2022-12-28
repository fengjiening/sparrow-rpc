package com.fengjiening.sparrow.processor;

import com.fengjiening.sparrow.annotation.SparrowAutowired;
import com.fengjiening.sparrow.annotation.SparrowServer;
import com.fengjiening.sparrow.enums.Sparrow;
import com.fengjiening.sparrow.proxy.SparrowProxy;
import com.fengjiening.sparrow.utill.LogInterceptor;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @ClassName: ClientPostProcessor
 * @Description: springbean初始化之前执行
 * @Date: 2022/11/2 11:13
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Data
public class ClientPostProcessor implements BeanPostProcessor {
   private Sparrow type;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        // 遍历bean的属性，找到有SparrowServer注解的属性
        for (Field field : fields) {

            if(field.isAnnotationPresent(SparrowAutowired.class)){
                try{
                    SparrowAutowired annotation = field.getAnnotation(SparrowAutowired.class);
                    int version = annotation.version();
                    Object instance= SparrowProxy.instance(field.getType(), version);
                    field.setAccessible(true);
                    field.set(bean, instance);
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
