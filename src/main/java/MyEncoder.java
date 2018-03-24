import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.base64.Base64;

import java.util.List;
import static io.netty.buffer.Unpooled.*;
public class MyEncoder extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int count=msg.capacity();
        ByteBuf ms=  buffer();
        ms.writeInt(count);
        out.add( copiedBuffer(ms,msg));
        System.out.println("chek "+ count);
    }
}
