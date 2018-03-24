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
        String s      = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "\n" +
            "    <groupId>123</groupId>\n" +
            "    <artifactId>345</artifactId>\n" +
            "    <version>1.0-SNAPSHOT</version>\n" +
            "    <build>\n" +
            "        <plugins>\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-compiler-plugin</artifactId>\n" +
            "                <configuration>\n" +
            "                    <source>1.6</source>\n" +
            "                    <target>1.6</target>\n" +
            "                </configuration>\n" +
            "            </plugin>\n" +
            "        </plugins>\n" +
            "    </build>\n" +
            "\n" +
            "    <dependencies>\n" +
            "\n" +
            "        <dependency>\n" +
            "            <groupId>io.netty</groupId>\n" +
            "            <artifactId>netty-all</artifactId> <!-- Use 'netty-all' for 4.0 or above -->\n" +
            "            <version>4.1.16.Final</version>\n" +
            "            <scope>compile</scope>\n" +
            "        </dependency>\n" +
            "    </dependencies>\n" +
            "</project>";

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
