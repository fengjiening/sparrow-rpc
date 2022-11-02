package com.fengjiening.sparrow.processor;

import com.fengjiening.sparrow.enums.Sparrow;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @ClassName: ServerPostProcessor
 * @Description: TODO
 * @Date: 2022/11/2 11:13
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Data
public class ServerPostProcessor implements BeanPostProcessor {
    private Sparrow type;


}
