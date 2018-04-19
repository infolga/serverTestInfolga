import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Runnable_SEND_CONVERSATION_NOTIFICATION implements Runnable {

    private Conversation conversation;
    private Map<String, ChannelHandlerContext> conectList;
    private PoolingDB db;
    private Connection con;
    private Statement stat;
    private int user_id;

    public Runnable_SEND_CONVERSATION_NOTIFICATION(Conversation conversation, int user_id, Map<String, ChannelHandlerContext> conectLis, PoolingDB db) throws SQLException {
        this.conversation = conversation;
        this.conectList = conectLis;
        this.db = db;
        this.user_id = user_id;
        con = db.getConnection();
    }

    @Override
    public void run() {
        try {

            Log.info("Runnable_SEND_CONVERSATION_NOTIFICATION");

            stat = con.createStatement();

            int conversation_id = conversation.getConversation_id();

            ArrayList<User_token> list = SQL.SQL_get_Array_users_id_token_device_token_where_user_id(stat, user_id);
            MyXML myXML = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_CONVERSATION_ADD);
            myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
            myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
            myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));

            myXML.addChildElement(conversation.getXMLElement());

            ArrayList<Participants> myins = SQL.SQL_get_Array_participants_from_participants_where_conversation_id(stat, conversation_id);
            for (int i = 0; i < myins.size(); i++) {
                myXML.addChildElement(myins.get(i).getXMLElement());
            }

            Message.Builder builder = Message.builder();
            builder.putData("type2", "conversation");
            if (conversation.getType().equals("single")) {
                builder
                    .setNotification(new Notification("New chat created", conversation.getName_conversation()))
                //.setNotification( new Notification("тест ", "ХА ХА?"));
                ;
            } else if (conversation.getType().equals("group")) {

                builder
                    .setNotification(new Notification("You were added to the conversation", conversation.getName_conversation()))
                //.setNotification( new Notification("тест ", "ХА ХА?"));
                ;

            }

            for (int i = 0; i < list.size(); i++) {
                ChannelHandlerContext channelHandlerContext = conectList.get(list.get(i).token);
                if (channelHandlerContext != null) {
                    if (channelHandlerContext.channel().isActive()) {
                        channelHandlerContext.write(myXML.toString());
                        channelHandlerContext.flush();
                    }
                }
                System.out.println(list.get(i).device_token);
                builder.setToken(list.get(i).device_token);
            }


            ArrayList<User_token> list2 = SQL.SQL_get_Array_users_id_token_device_token_in_conversation(stat, conversation_id);

            myXML.setRootName(MSG.XML_TYPE_RESPONSE);

            for (int i = 0; i < list2.size(); i++) {
                ChannelHandlerContext channelHandlerContext = conectList.get(list2.get(i).token);
                if (channelHandlerContext != null) {
                    if (channelHandlerContext.channel().isActive()) {
                        if (list2.get(i).users_id != user_id) {
                            channelHandlerContext.write(myXML.toString());
                            channelHandlerContext.flush();
                        }
                    } else {
                        conectList.remove(list.get(i).token);
                    }
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
