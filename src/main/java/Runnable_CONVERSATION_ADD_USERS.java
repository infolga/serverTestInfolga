import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Runnable_CONVERSATION_ADD_USERS implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    private QueueTask QT;


    public Runnable_CONVERSATION_ADD_USERS(MyXML myXML, ChannelHandlerContext ctx, PoolingDB db, QueueTask Q) throws SQLException {
        this.myXML = myXML;
        this.ctx = ctx;
        this.db = db;
        con = db.getConnection();
        QT = Q;
    }

    @Override
    public void run() {
        try {

            Log.info("Runnable_CONVERSATION_ADD_USERS");
            Log.info(myXML.toString());
            con.setAutoCommit(false);
            stat = con.createStatement();

            String token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);
            int user_id = SQL.SQL_select_users_id_from_access_where_token(stat, token);


            if (user_id != -1) {// токен найден и рабочий
                int conv_id = Integer.parseInt(myXML.getValueInActionsXML(MSG.XML_ELEMENT_CONVERSATION_ID));
                int user_id2 = Integer.parseInt(myXML.getValueInActionsXML(MSG.XML_ELEMENT_USERS_ID));

                Conversation conversation = SQL.SQL_select_conversation_all_from_conversation_where_conversation_id_users_id(stat, conv_id, user_id);

                if (conversation.getType().equals("group")) {
                    SQL.SQL_insert_into_participants_conversation_id_users_id(stat, conv_id, user_id2);

                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.addChildElement(conversation.getXMLElement());

                    ArrayList<Participants> myins = SQL.SQL_get_Array_participants_from_participants_where_conversation_id(stat, conv_id);
                    for (int i = 0; i < myins.size(); i++) {
                        myXML.addChildElement(myins.get(i).getXMLElement());
                    }

                    MyTask task =new MyTask(conversation,null,MyTask.SYSTEM_MSG);
                    task.arg1 = user_id2;
                    QT.addTask(task);


                }else {

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
            Log.info(myXML.toString());
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
