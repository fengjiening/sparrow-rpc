package com.fengjiening.sparrow.config.vo;

import com.fengjiening.sparrow.enums.ResolverType;
import com.fengjiening.sparrow.enums.SerializeType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName: RemotingCommand
 * @Description: 消息传输过程中的对数据内容的封装类
 * @Date: 2022/10/27 11:22
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Builder
@Data
@ToString
public class RemotingCommand implements Serializable {
    private SerializeType serializeType;
    private int protocolCode;
    private ResolverType resolverType;
    private int id;
    private byte[] token;
    private int length;
    private byte[] body;
}
