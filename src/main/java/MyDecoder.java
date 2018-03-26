import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.mortbay.log.Log;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {

    private int count;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

        count = -1;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

       // Log.info("handlerAdded  count= " + count);
       // Log.info("handlerAdded  msg.readableBytes()  = " + msg.readableBytes());

        if (count == -1) {
            if (msg.readableBytes() < 4) {
                return;
            } else {
                count = msg.readInt();
            }
        } else {

            if (msg.readableBytes() < count) {
                return;
            } else {

                out.add(msg.readBytes(count));
                count = -1;
            }

        }


    }
}
