package com.fengjiening.sparrow.server;

/**
 * @ClassName: SparrowPlatform
 * @Description: TODO
 * @Date: 2022/10/31 14:10
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public interface SparrowPlatform {
    public void startUp();
    public void shutDown() throws Exception;
}
