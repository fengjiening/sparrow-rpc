package com.fengjiening.sparrow.manager;

import com.fengjiening.sparrow.config.SparrowDecoder;
import com.fengjiening.sparrow.config.SparrowEncoder;
import com.fengjiening.sparrow.config.protocol.SparrowProtocol;
import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.context.SparrowContext;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.entity.RpcRequest;
import com.fengjiening.sparrow.enums.ResolverType;
import com.fengjiening.sparrow.enums.SerializeType;
import com.fengjiening.sparrow.serializer.SparrowSerializer;
import com.fengjiening.sparrow.utill.SparrowOptional;
import com.fengjiening.sparrow.utill.UUIDUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @ClassName: CommandManager
 * @Description: 参数管理类
 * @Date: 2022/11/2 09:34
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class CommandManager {

    public RemotingCommand createRequest(String id,Object obj) {
        SparrowSerializer serializer = SparrowContext.rpcProtocol.getSerializer();
        RemotingCommand.RemotingCommandBuilder builder = createHeader(id,serializer);
        if(obj instanceof RpcRequest){
            RpcRequest request = (RpcRequest) obj;
            byte[] content = serializer.serialize(request);
            int length = content.length+ SparrowProtocol.HEADER_LENGTH;
            return builder.length(length)
                    .body(content)
                    .build();
        }else {
            SparrowOptional.throwsException(CommonConstant.SPARROW_TYPE_COMMON_ERROR_CODE,"非法rpc请求实体");
        }
        return null;
    }
    //public RemotingCommand createResponse(int id, Object data, CommandCode commandCode) {
    //    RpcRemotingCommand.RpcRemotingCommandBuilder builder = createHeader(id, commandCode);
    //    if(data instanceof RpcResponse){
    //        RpcResponse response = (RpcResponse) data;
    //        // 序列化
    //        Serializer serializer = SerializerManager.getSerializer(DEFAULT_SERIALIZER);
    //        byte[] content = serializer.serialize(response, RpcResponse.class);
    //        // 计算length和crc32
    //        return builder.length(content.length + RpcProtocol.HEADER_LENGTH)
    //                .crc32(crc32(content))
    //                .content(content)
    //                .build();
    //    }
    //    else if(data instanceof String){
    //        byte[] content = ((String) data).getBytes(StandardCharsets.UTF_8);
    //        return builder.length(content.length + RpcProtocol.HEADER_LENGTH)
    //                .crc32(crc32(content))
    //                .content(content)
    //                .build();
    //    }
    //    return null;
    //}
    private  RemotingCommand.RemotingCommandBuilder createHeader(String id, SparrowSerializer serializer){
        String token= UUIDUtil.getConcurrentUUID(SparrowContext.contextUuid);
       return  RemotingCommand.builder()
                .id(id)
                .protocolCode(SparrowProtocol.protocolCode)
                .token(serializer.serialize(token))
                .serializeType(serializer.type());
    }
}
