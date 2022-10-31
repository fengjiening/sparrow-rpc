package com.fengjiening.sparrow.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * @ClassName: SerializeType
 * @Description: 序列化枚举
 * @Date: 2022/10/27 11:23
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public enum SerializeType {
    JSON((byte) 0),
    SPARROW((byte) 1);

    private byte code;
    SerializeType(byte code) {
        this.code = code;
    }
    public byte getValue() {
        return code;
    }
    public void setValue(byte code) {
        this.code = code;
    }
    public static SerializeType find(byte value){
        Optional<SerializeType> type=  Arrays.stream(SerializeType.values()).filter(e -> e.code==value).findFirst();
        return type.get();
    }
}
