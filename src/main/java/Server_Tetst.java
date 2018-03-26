import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server_Tetst {

    private   int port;
    private final QueueTask QT;
    private ServerBootstrap b;
    private ChannelFuture f;

    public Server_Tetst(QueueTask Q) {

        QT = Q;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new MyChannelInitializer(QT));


        //  f.channel().closeFuture().sync();

    }

    public ChannelFuture bind(int p) throws InterruptedException {
        port = p;
        f = b.bind(port).sync();
        return f;
    }

    public void close() {
        f.channel().close();
    }

}

