import com.google.firebase.messaging.*;
import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Runnable_SEND_MESSAGES_NOTIFICATION implements Runnable {

    private Messages messages;
    private Map<String, ChannelHandlerContext> conectList;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    public Runnable_SEND_MESSAGES_NOTIFICATION(Messages message, Map<String, ChannelHandlerContext> conectLis, PoolingDB db) throws SQLException {
        this.messages = message;
        this.conectList = conectLis;
        this.db = db;
        con = db.getConnection();
    }

    @Override
    public void run() {
        try {

            Log.info("Runnable_SEND_MESSAGES_NOTIFICATION");


            stat = con.createStatement();

            int conversation_id = messages.getConversation_id();

            ArrayList<User_token> list = SQL.SQL_get_Array_users_id_token_device_token_in_conversation(stat, conversation_id);
            MyXML myXML = new MyXML(MSG.XML_TYPE_RESPONSE, MSG.XML_ADD_MESSAGES);
            myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
            myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
            myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
            myXML.addChildElement(messages.getXMLElement());




            User user = SQL.SQL_get_users_from_users_where_id(stat, messages.getSender_id());



//                .setAndroidConfig(AndroidConfig.builder()
//                    //.setTtl(3600 * 1000) // 1 hour in milliseconds
//                    .setPriority(AndroidConfig.Priority.NORMAL)
//                    .setNotification(AndroidNotification.builder()
//                        .setTitle( user.getFirst_name() + " " + user.getLast_name())
//                        .setBody(messages.getMessage())
//                        // .setIcon("stock_ticker_update")
//                        .setColor("#f45342")
//                        .build())
//                    .build());





//            builder.putData("id", Integer.toString(messages.getId()))
//                .putData("id", Integer.toString(messages.getId()))
//                .putData("conversation_id", Integer.toString(messages.getConversation_id()))
//                .putData("sender_id", Integer.toString(messages.getSender_id()))
//                .putData("message_type2", messages.getMessage_type())
//                .putData("message", messages.getMessage())
//                .putData("attachment_thumb_url", messages.getAttachment_thumb_url())
//                .putData("attachment_url", messages.getAttachment_url())
//                .putData("created_at", messages.getCreated_at())
//                .putData("us_FL_name", user.getFirst_name() + " " + user.getLast_name())
//            ;

            Message.Builder builder = Message.builder();
            builder
                .setNotification(new Notification(user.getFirst_name() + " " + user.getLast_name(), messages.getMessage()))
                //.setNotification( new Notification("тест ", "ХА ХА?"));
            ;
            builder.putData("type2", "messages");
            for (int i = 0; i < list.size(); i++) {

                ChannelHandlerContext channelHandlerContext = conectList.get(list.get(i).token);


                if (channelHandlerContext != null) {
                    if (channelHandlerContext.channel().isActive()) {
                        channelHandlerContext.write(myXML.toString());
                        channelHandlerContext.flush();
                    } else {
                        conectList.remove(list.get(i).token);

                        System.out.println(list.get(i).device_token);
                        builder.setToken(list.get(i).device_token);
                    }
                } else {
                    System.out.println(list.get(i).device_token);
                        builder.setToken(list.get(i).device_token);
                }

            }

            Message mess = builder.build();
            try {
                System.out.println(FirebaseMessaging.getInstance().sendAsync(mess).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //  FirebaseMessaging.getInstance().sendAsync(message).get();


        } catch (SQLException e) {

            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            try {
                if (stat != null) stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (con != null) {

                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
