package com.fengjiening.sparrow.config.protocol;

import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.protocol.Protocol;

/**
 * @ClassName: Protocol
 * @Description: 基础协议入口
 * @Date: 2022/10/26 16:54
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public abstract class SparrowProtocol implements Protocol {
     private SparrowDecoder decoder;
     private SparrowEncoder encoder;
    /**
     * 协议码
     */
    public static final byte protocolCode = 0x11;
    public static final int HEADER_LENGTH = 39;
    public static final int TOKEN_LENGTH = 32;



    public SparrowDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(SparrowDecoder decoder) {
        this.decoder = decoder;
    }

    public SparrowEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(SparrowEncoder encoder) {
        this.encoder = encoder;
    }
}
