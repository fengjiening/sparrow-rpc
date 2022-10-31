package com.fengjiening.sparrow.enums;


import java.util.Arrays;
import java.util.Optional;

/**
 * @ClassName: SerializeType
 * @Description: 解析器类型枚举
 * @Date: 2022/10/27 11:23
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public enum ResolverType {
    REQUEST((byte)1), RESPONSE((byte)2), PING((byte)3), PONG((byte)4), EMPTY((byte)5), UNKNOW((byte)6);

    private byte type;

    ResolverType(byte type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ResolverType find(byte type){
        Optional<ResolverType> resolverType=  Arrays.stream(ResolverType.values()).filter(e -> e.type==type).findFirst();
        if(resolverType.get()==null){
            //throw new SparrowException(new SparrowCode(CommonConstant.SPARROW_TYPE_NOT_FIND_ERROR_CODE),String.format("%s is unsupported",type));
            return  ResolverType.UNKNOW;
        }
        return resolverType.get();
    }
}
