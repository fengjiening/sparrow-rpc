package com.fengjiening.sparrow.serializer;

import com.fengjiening.sparrow.enums.SerializeType;

import java.io.IOException;

/**
 * @ClassName: SparrowSerializer
 * @Description: 序列化接口
 * @Date: 2022/11/2 09:50
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public interface SparrowSerializer {

    public SerializeType type();
    /**
     * java对象转换为二进制
     *
     * @param obj
     * @return
     */
    public  <T> byte[] serialize(T obj);

    /**
     * 二进制转换成java对象
     *
     * @param clazz
     * @param data
     * @return
     */
    public  <T> T deserialize(byte[] data, Class<T> clazz);
}
