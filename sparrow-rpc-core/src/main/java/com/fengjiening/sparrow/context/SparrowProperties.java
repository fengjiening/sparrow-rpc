package com.fengjiening.sparrow.context;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @ClassName: SparrowProperties
 * @Description: TODO
 * @Date: 2022/10/26 10:43
 * @Author: fengjiening::joko
 * @Version: 1.0
 */

//@Configuration
@Data
public class SparrowProperties{
    // @Value 注解，通过 EL 表达式获取 【application.yml】 、【application.properties】 中的属性
    @Value(value = "${sparrow.rpc.protocol}")
    private String protocol;
    @Value(value = "${sparrow.rpc.resolver}")
    private String resolver;
    @Value(value = "${sparrow.rpc.client.port}")
    private Integer port;
    @Value(value = "${sparrow.rpc.client.maxidle}")
    private Integer maxidle;
    @Value(value = "${sparrow.rpc.client.minidle}")
    private Integer minidle;
    @Value(value = "${sparrow.rpc.client.maxtotal}")
    private Integer maxtotal;

}
