package com.fengjiening.sparrow.registry;

import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.protocol.local.LocalRegistry;
import com.fengjiening.sparrow.server.ServiceInfo;
import com.fengjiening.sparrow.spi.SPI;
import com.fengjiening.sparrow.utill.PropertiesUtil;
import com.fengjiening.sparrow.utill.ThreadPoolUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: Registry
 * @Description: 基类
 * @Date: 2022/10/24 15:56
 * @Author: fengjiening::joko
 * @Version: 0.0
 */
@SPI
public interface Registry {
    /**
     * 从远程注册中心拉取该名称的生产者节点集合
     * @param serviceName 服务名
     * @param version 服务版本号
     * @return {@link List <ProviderNode>} 服务提供者集合
     */
    Set<ProviderNode> lookupProviders(String serviceName, int version);

    /**
     * 生产者节点通过该方法将自己注册到注册中心
     * @param services 服务集合
     * @param node Provider 信息
     */
    void registerProvider(List<ServiceInfo> services, ProviderNode node);

    /**
     * 初始化registry
     */
    void init();
    public static final long REGISTER_TIMEOUT = 60 * 1000;
    /**
     * 开启心跳
     * @param services 提供服务集合
     * @param node ProviderNode
     */
    default void startHeartBeat(List<ServiceInfo> services, ProviderNode node){
        int port = PropertiesUtil.getInt("sparrow.rpc.client.port",10101);
        ThreadPoolUtil.scheduleAtFixedRate(()->{
            node.setLastHeartBeatTime(System.currentTimeMillis());
            heatBeat(services, node);
        }, CommonConstant.REGISTER_TIMEOUT / 2, CommonConstant.REGISTER_TIMEOUT / 2, TimeUnit.MILLISECONDS);
    }

    /**
     * 心跳
     * @param services 服务列表
     * @param node 提供者信息
     */
    void heatBeat(List<ServiceInfo> services, ProviderNode node);

    /**
     * 设置本地注册中心
     * @param localRegistry {@link LocalRegistry}
     */
    void setLocalRegistry(LocalRegistry localRegistry);
}
