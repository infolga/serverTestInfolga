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
        ctx.write("1zwexrctbyuhjmisxdrctfvgybhunjimksxdrcfvgybhnjimkoazsexdrcfvgbhunjimfghjkgdhjkl;'sfdghjkl;'dfghj+klgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklmklgmdfsklgmfdsklhmgklm,vmb ,.x ,vsd,l;cs;dav");
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
