import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server_Tetst {
    public static void main(String[] ar) {
        String PORT = System.getProperty("port", "test_port");
        // Address to bind on / connect to
        //
        int port = 8080;
        final LocalAddress addr = new LocalAddress(PORT);

        PoolingDB.instanse();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline().addLast("MyEncoder", new MyEncoder());
                        ch.pipeline().addLast("MyDecoder", new MyDecoder());

                        ch.pipeline().addLast("Base64Decoder", new Base64Decoder());
                        ch.pipeline().addLast("Base64Encoder", new Base64Encoder());

                        ch.pipeline().addLast("StringDecoder", new StringDecoder());
                        ch.pipeline().addLast("StringEncoder", new StringEncoder());


                        ch.pipeline().addLast("My", new DecodeToTask());

                    }
                });

            ChannelFuture f = b.bind(port).sync();
            System.out.println("ready");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
