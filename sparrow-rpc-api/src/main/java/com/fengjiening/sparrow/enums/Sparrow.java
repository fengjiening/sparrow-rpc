package com.fengjiening.sparrow.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * @ClassName: Sparrow
 * @Description: 服务类型
 * @Date: 2022/10/27 11:23
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public enum Sparrow {
    Server((byte) 0),
    Slient((byte) 1),
    All((byte) 2);

    private byte code;
    Sparrow(byte code) {
        this.code = code;
    }
    public byte getValue() {
        return code;
    }
    public void setValue(byte code) {
        this.code = code;
    }
    public static Sparrow find(byte value){
        Optional<Sparrow> type=  Arrays.stream(Sparrow.values()).filter(e -> e.code==value).findFirst();
        return type.get();
    }
}
