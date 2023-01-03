package com.fengjiening.sparrow.loadbalance;

import com.fengjiening.sparrow.entity.RpcRequest;
import com.fengjiening.sparrow.registry.ProviderNode;
import com.fengjiening.sparrow.spi.SPI;


import java.util.Set;

/**
 * <p>
 *  负载均衡接口
 * </p>
 *
 * @author Jay
 * @date 2022/02/08 13:19
 */
@SPI
public interface LoadBalance {

    /**
     * 选择provider
     * @param providerNodes provider集合
     * @param request {@link RpcRequest}
     * @return {@link ProviderNode}
     */
    ProviderNode select(Set<ProviderNode> providerNodes, RpcRequest request);
}
