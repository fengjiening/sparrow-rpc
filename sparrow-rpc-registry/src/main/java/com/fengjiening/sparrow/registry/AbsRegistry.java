package com.fengjiening.sparrow.registry;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: AbsRegistry
 * @Description: TODO
 * @Date: 2022/10/24 15:56
 * @Author: fengjiening::joko
 * @Version: 0.0
 */
public abstract class AbsRegistry implements Registry {
    /**
     * 客户端本地注册中心缓存
     */
    private final ConcurrentHashMap<String, Set<ProviderNode>> registryCache = new ConcurrentHashMap<String, Set<ProviderNode>>(256);

}
