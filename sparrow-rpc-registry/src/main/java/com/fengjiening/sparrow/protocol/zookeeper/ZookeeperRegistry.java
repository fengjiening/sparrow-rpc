package com.fengjiening.sparrow.protocol.zookeeper;

import com.alibaba.fastjson2.JSON;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.protocol.local.LocalRegistry;
import com.fengjiening.sparrow.registry.ProviderNode;
import com.fengjiening.sparrow.registry.Registry;
import com.fengjiening.sparrow.server.ServiceInfo;
import com.fengjiening.sparrow.utill.PropertiesUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
/**
 * @ClassName: ZooKeeperRegistry
 * @Description: zookeeper 注册中心
 * @Date: 2022/10/24 16:06
 * @Author: fengjiening::joko
 * @Version: 0.0
 */
@Slf4j
public class ZookeeperRegistry implements Registry {
    /**
     * 本地注册中心缓存
     */
    private LocalRegistry localRegistry;
    /**
     * Zookeeper客户端
     */
    private CuratorFramework client;



    @Override
    public Set<ProviderNode> lookupProviders(String serviceName, int version) {
        String path = getPath(serviceName, version);
        Set<ProviderNode> result = new HashSet<>();
        try {
            List<String> children = client.getChildren().forPath(path);
            for (String child : children) {
                String childPath = path + "/" + child;
                byte[] data = client.getData().forPath(childPath);
                String json = new String(data, CommonConstant.DEFAULT_CHARSET);
                ProviderNode node = JSON.parseObject(json, ProviderNode.class);
                result.add(node);
            }
            log.info("Look up service done, service: {}, providers: {}", path, result);
        }catch (Exception e){
            log.error("Look up service error, service: {}_{}", serviceName+version, e);
        }
        return result;
    }

    @Override
    public void registerProvider(List<ServiceInfo> services, ProviderNode node) {
        String json = JSON.toJSONString(node);
        byte[] data = json.getBytes(CommonConstant.DEFAULT_CHARSET);
        String url = node.getUrl();
        for (ServiceInfo service : services) {
            try{
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                        .forPath(getPath(service.getType().getName(), service.getVersion()) + "/" + url, data);
            }catch (Exception e){
                log.error(String.format("Register Provider Error, Service: {}_{}", service.getType(), service.getVersion()), e);
            }
        }
    }

    @Override
    public void init() {
        // 加载ZK options

        String host = PropertiesUtil.get("sparrow.registry.zookeeper.host","127.0.0.1");
       int port = PropertiesUtil.getInt("sparrow.registry.zookeeper.port",2181);
        int sessionTimeout = CommonConstant.ZOOKEEPER_SESSION_TIMEOUT;
        int connectionTimeout = CommonConstant.ZOOKEEPER_CONNECTION_TIMEOUT;
        long start = System.currentTimeMillis();
        // 创建curator客户端
        client = CuratorFrameworkFactory.newClient(host + ":" + port, sessionTimeout, connectionTimeout, new RetryOneTime(1000));
        // 启动客户端
        client.start();
        log.info("Zookeeper 注册中心客户端启动完成，用时：{}ms", (System.currentTimeMillis() - start));
        try{
            // 开启TreeCache，监听Node变化
            TreeCache treeCache = TreeCache.newBuilder(client, CommonConstant.ZOOKEEPER_ROOT_PATH)
                    .setCacheData(true)
                    .build();
            treeCache.getListenable().addListener(new RegistryListener());
            treeCache.start();
        }catch (Exception e){
            log.error("Start TreeCache Failed ", e);
        }
    }

    @Override
    public void heatBeat(List<ServiceInfo> services, ProviderNode node) {

    }

    @Override
    public void setLocalRegistry(LocalRegistry localRegistry) {
        this.localRegistry = localRegistry;
    }


    class RegistryListener extends AbstractTreeCacheListener{

        @Override
        public void onNodeDataChanged(String path, byte[] data) {

        }

        @Override
        public void onNodeCreated(String path, byte[] data) {
            if(!StringUtils.isEmpty(path) && data != null && data.length > 0){
                ServiceInfo serviceInfo = getServiceInfo(path);
                if(serviceInfo != null){
                    // 注册新的Provider到本地Registry
                    String json = new String(data, CommonConstant.DEFAULT_CHARSET);
                    ProviderNode node = JSON.parseObject(json, ProviderNode.class);
                    localRegistry.registerProvider(serviceInfo.getType().getName(), serviceInfo.getVersion(), node);
                }
            }

        }

        @Override
        public void onNodeDeleted(String path)  {
            ServiceInfo serviceInfo = getServiceInfo(path);
            if(serviceInfo != null){
                log.info("Provider offline: {}", path);
                // node被删除，Provider下线
                localRegistry.onProviderOffline(getUrl(path), serviceInfo.getType().getName(), serviceInfo.getVersion());
            }
        }
        private ServiceInfo getServiceInfo(String path){
            int idx = path.indexOf(CommonConstant.ZOOKEEPER_ROOT_PATH);
            if(idx != -1) {
                String substring = path.substring(idx + CommonConstant.ZOOKEEPER_ROOT_PATH.length() + 1);
                String[] parts = substring.split("/");
                int version = Integer.parseInt(parts[1]);
                Class<?> type = ClassUtils.resolveClassName(parts[0], getClass().getClassLoader());
                return new ServiceInfo(type, version);
            }
            return null;
        }

        private String getUrl(String path){
            int i = path.lastIndexOf("/");
            return path.substring(i + 1);
        }
    }

    private String getPath(String serviceName, int version){
        return CommonConstant.ZOOKEEPER_ROOT_PATH + "/" + serviceName + "/" + version;
    }

}

