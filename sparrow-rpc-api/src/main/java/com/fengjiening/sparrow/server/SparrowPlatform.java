package com.fengjiening.sparrow.server;

/**
 * @ClassName: SparrowPlatform
 * @Description: TODO
 * @Date: 2022/10/31 14:10
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public abstract class SparrowPlatform {
    public SparrowPlatform(){
        init();
        startUp();
    }
    public abstract void init();
    public abstract void startUp();
    public abstract void shutDown() throws Exception;
}
