import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static io.netty.buffer.Unpooled.buffer;
import static io.netty.buffer.Unpooled.copiedBuffer;


public class MyencodeToByteBuf extends MessageToMessageEncoder<byte[]> {


    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {

        ByteBuf ms = buffer();
        ms.writeBytes((byte[]) msg);
        out.add(ms);
    }
}

