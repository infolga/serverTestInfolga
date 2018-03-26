import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

public class MyRunnableTask implements Runnable {

    private String msgXML;
    private ChannelHandlerContext ctx;

    public MyRunnableTask( ChannelHandlerContext ctx,String msgXML) {
        this.msgXML = msgXML;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        Log.info(  msgXML);
    }
}
