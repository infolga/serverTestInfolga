import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Runnable_GET_MESSAGES_FO_TIME implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    private QueueTask QT;


    public Runnable_GET_MESSAGES_FO_TIME(MyXML myXML, ChannelHandlerContext ctx, PoolingDB db, QueueTask Q) throws SQLException {
        this.myXML = myXML;
        this.ctx = ctx;
        this.db = db;
        con = db.getConnection();
        QT = Q;
    }
    @Override
    public void run() {
        try {

            Log.info("Runnable_GET_MESSAGES_FO_TIME");
            Log.info(myXML.toString());
            con.setAutoCommit(false);
            stat = con.createStatement();


            String token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);

            Messages messages = new Messages(myXML.getCildElement(MSG.XML_ELEMENT_MESSAGES));
            int count = Integer.parseInt(myXML.getValueInActionsXML(MSG.XML_ELEMENT_COUNT));


            int user_id = SQL.SQL_select_users_id_from_access_where_token(stat, token);

            if (user_id != -1) {// токен найден и рабочий


                int participant_id = SQL.SQL_select_participant_id_from_participants_where_user_id_conversation_id(stat, user_id, messages.getConversation_id());
                if (participant_id != -1) {// пользоветель состоит в этой беседе
                    ArrayList<Myin> myins = SQL.SQL_get_Array_messages_from_messagess_where_conversation_id_date_limit(stat, messages.getConversation_id(), messages.getCreated_at(), messages.getId(), count);


                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.removeChild(MSG.XML_ELEMENT_MESSAGES);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));

                    for (int i = 0; i < myins.size(); i++) {
                        myXML.addChildElement(myins.get(i).getXMLElement());
                    }


                } else {

                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INSUFFICIENT_ACCESS_RIGHTS));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INSUFFICIENT_ACCESS_RIGHTS));

                }

            } else {// токен недействительный
                myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_TOKEN_UNAVAILABLE));
                myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_TOKEN_UNAVAILABLE));
            }


            con.commit();

             System.out.println(myXML.toString());

            ctx.write(myXML.toString());
            ctx.flush();
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
