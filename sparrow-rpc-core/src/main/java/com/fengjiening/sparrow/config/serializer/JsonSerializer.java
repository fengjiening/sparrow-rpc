package com.fengjiening.sparrow.config.serializer;

import com.alibaba.fastjson2.JSON;
import com.fengjiening.sparrow.enums.SerializeType;
import com.fengjiening.sparrow.serializer.SparrowSerializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON序列化
 */
public class JsonSerializer implements SparrowSerializer {

    @Override
    public SerializeType type() {
        return SerializeType.JSON;
    }

    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }
}
