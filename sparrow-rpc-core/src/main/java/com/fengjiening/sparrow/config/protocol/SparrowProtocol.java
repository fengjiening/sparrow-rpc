package com.fengjiening.sparrow.config.protocol;

import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.protocol.Protocol;
import com.fengjiening.sparrow.serializer.SparrowSerializer;
import lombok.Data;

/**
 * @ClassName: Protocol
 * @Description: 基础协议入口
 * @Date: 2022/10/26 16:54
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Data
public  class SparrowProtocol implements Protocol {
     private SparrowDecoder decoder;
     private SparrowEncoder encoder;
     private SparrowSerializer serializer;
    /**
     * 协议码
     */
    public static final byte protocolCode = 0x11;
    public static final int ID_LENGTH = 18;
    public static final int HEADER_LENGTH = 37+ID_LENGTH;
    public static final int TOKEN_LENGTH = 32;

}
