import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.mortbay.log.Log;

public class DecodeToTask extends SimpleChannelInboundHandler  {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

       // Log.info("channelRead0"+ctx.toString());
        Log.info("msg   "+msg.toString());
       // Log.info(" "+(int)msg.toString().charAt(1));
        System.out.println("channelRead0");
        String s      =  "15.01.2018 22:05:10\tРазряжается\t87.7%\t31 820\t-3280\t11 699\tТаймер\t\n" +
            "15.01.2018 22:05:40\tРазряжается\t87.6%\t31 790\t-3220\t11 703\tТаймер\t\n" +
            "15.01.2018 22:06:10\tРазряжается\t87.5%\t31 750\t-3310\t11 669\tТаймер\t\n" +
            "15.01.2018 22:06:40\tРазряжается\t87.4%\t31 720\t-3270\t11 693\tТаймер\t\n" +
            "15.01.2018 22:07:10\tРазряжается\t87.3%\t31 690\t-3260\t11 693\tТаймер\t\n" +
            "15.01.2018 22:07:40\tРазряжается\t87.2%\t31 660\t-3260\t11 652\tТаймер\t";

        ctx.write(s);
            ctx.flush();

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        Log.info("Registered"+ctx.toString());
        System.out.println("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        Log.info("Unregistered"+ctx.toString());
        System.out.println("channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.info("Active"+ctx.toString());
        System.out.println("channelActive");


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.info("Inactive"+ctx.toString());
        System.out.println("channelInactive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
       // Log.info("channelReadComplete"+ctx.toString());
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
        Log.info("WritabilityChanged"+ctx.toString());
        System.out.println("channelWritabilityChanged");
    }
}
