import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.xml.XmlDecoder;
import io.netty.handler.codec.xml.XmlFrameDecoder;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private QueueTask QT;

    public MyChannelInitializer(QueueTask QT) {
        this.QT = QT;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("MyEncoder", new MyEncoder());
        ch.pipeline().addLast("MyDecoder", new MyDecoder());

        ch.pipeline().addLast("Base64Decoder", new Base64Decoder());
        ch.pipeline().addLast("Base64Encoder", new Base64Encoder());

        //ch.pipeline().addLast("StringDecoder", new StringDecoder());
        ch.pipeline().addLast("StringEncoder", new StringEncoder());

        ch.pipeline().addLast("MyD", new DecodeToTask(QT));
        //ch.pipeline().addLast("MyE", new MyencodeToByteBuf());

    }
}
