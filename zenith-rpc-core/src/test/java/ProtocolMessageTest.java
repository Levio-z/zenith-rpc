import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.zenith.zenithrpc.constant.RpcConstant;
import com.zenith.zenithrpc.model.RpcRequest;
import com.zenith.zenithrpc.protocol.*;
import com.zenith.zenithrpc.server.tcp.ProtocolMessageDecoder;
import com.zenith.zenithrpc.server.tcp.ProtocolMessageEncoder;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ProtocolMessageTest {
    @Test
    public void testEncodeAndDecode() throws IOException {
        // 构造消息
        ProtocolMessage<RpcRequest> message = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getCode());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"aaa", "bbb"});

        message.setHeader(header);
        message.setBody(rpcRequest);

        Buffer encodeBuffer = ProtocolMessageEncoder.encode(message);
        ProtocolMessage<?> decodeMessage = ProtocolMessageDecoder.decode(encodeBuffer);
        System.out.println(JSONUtil.toJsonStr(decodeMessage));
        Assert.assertNotNull(decodeMessage);


    }
}
