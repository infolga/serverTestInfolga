

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.xml.XmlDecoder;
import io.netty.handler.codec.xml.XmlElement;
import io.netty.handler.codec.xml.XmlFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.net.*;
        import java.io.*;
public class Server_Tetst {
    public static void main(String[] ar)    {
         String PORT = System.getProperty("port", "test_port");
        // Address to bind on / connect to
        //
        int port =8080;
        final LocalAddress addr = new LocalAddress(PORT);


        EventLoopGroup bossGroup = new OioEventLoopGroup();
        EventLoopGroup workerGroup = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(OioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast("MyEncoder", new MyEncoder());
                            ch.pipeline().addLast("Base64Decoder", new Base64Decoder());

                           // ch.pipeline().addLast("decoder", new MyXmlDecoder());//декодирует приходящие данные в строку
                            ch.pipeline().addLast("Base64Encoder", new Base64Encoder());
                            ch.pipeline().addLast("StringDecoder", new StringDecoder());
                            ch.pipeline().addLast("StringEncoder", new StringEncoder());


                            ch.pipeline().addLast("My", new DecodeToTask());

                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
