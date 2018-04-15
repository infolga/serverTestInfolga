import io.netty.channel.ChannelHandlerContext;

public class MyTask {

    public static final int PACKEGE_MSG = 1;
    public static final int SYSTEM_MSG = 2;

    public final Object str;
    public final ChannelHandlerContext ctx;
    public final int MSG;

    public MyTask(Object str, ChannelHandlerContext ctx, int MSG) {
        this.str = str;
        this.ctx = ctx;
        this.MSG = MSG;
    }
}
