import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

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

            con.setAutoCommit(false);
            stat = con.createStatement();

            int conversation_id = messages.getConversation_id();

            ArrayList<User_token> list = SQL.SQL_get_Array_users_id_token_device_token(stat, conversation_id);
            MyXML myXML = new MyXML(MSG.XML_TYPE_RESPONSE, MSG.XML_ADD_MESSAGES);
            myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
            myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);

            myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
            myXML.addChildElement(messages.getXMLElement());

            Message.Builder builder = Message.builder();

            for (int i = 0; i < list.size(); i++) {

                ChannelHandlerContext channelHandlerContext = conectList.get(list.get(i).token);

                if (channelHandlerContext != null) {
                    channelHandlerContext.write(myXML.toString());
                    channelHandlerContext.flush();
                } else {

                }

            }



          //  FirebaseMessaging.getInstance().sendAsync(message).get();
            con.commit();


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
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
