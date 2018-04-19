import io.netty.channel.ChannelHandlerContext;
import org.jdom2.Element;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Runnable_CONVERSATION_ADD implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    private QueueTask QT;


    public Runnable_CONVERSATION_ADD(MyXML myXML, ChannelHandlerContext ctx, PoolingDB db, QueueTask Q) throws SQLException {
        this.myXML = myXML;
        this.ctx = ctx;
        this.db = db;
        con = db.getConnection();
        QT = Q;
    }

    @Override
    public void run() {
        try {

            Log.info("Runnable_CONVERSATION_ADD");
            // Log.info(myXML.toString());
            con.setAutoCommit(false);
            stat = con.createStatement();
            String user_id2 = null;
            String token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);
            int user_id = SQL.SQL_select_users_id_from_access_where_token(stat, token);
            Conversation conversation = null;
            if (user_id != -1) {// токен найден и рабочий


                String title = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TITLE);
                String name_conversation = myXML.getValueInActionsXML(MSG.XML_ELEMENT_NAME_CONVERSATION);
                String type = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TYPE);

                if (type.equals(MSG.XML_ELEMENT_TYPE_SINGLE)) {

                    Element user = myXML.getCildElement(MSG.XML_ELEMENT_USER);
                    user_id2 = user.getChild(MSG.XML_ELEMENT_USERS_ID).getText();
                    //проверка наличие беседы между двумя пользователями
                    int conv_id = SQL.SQL_get_common_conversation_id_single_where_users_id1_and_users_id2(stat, user_id, Integer.parseInt(user_id2));

                    Date created_at = SQL.getInstansInGreenwich().getTime();

                    if (conv_id == -1) {
                        conv_id = SQL.SQL_insert_into_conversation_title_name_conversation_photo_id_type_creator_id_created_at_updated_at(stat, title, name_conversation, 0, type, user_id, created_at);
                        // conv_id = SQL.SQL_select_conversation_id_from_conversation_conversation_title_name_conversation_photo_id_type_creator_id_created_at_updated_at(stat, title, "", 0, type, user_id, created_at);
                        SQL.SQL_insert_into_participants_conversation_id_users_id(stat, conv_id, user_id);
                        SQL.SQL_insert_into_participants_conversation_id_users_id(stat, conv_id, Integer.parseInt(user_id2));
                    }

                    conversation = SQL.SQL_select_conversation_all_from_conversation_where_conversation_id_users_id(stat, conv_id, user_id);

                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.addChildElement(conversation.getXMLElement());

                    ArrayList<Participants> myins = SQL.SQL_get_Array_participants_from_participants_where_conversation_id(stat, conv_id);
                    for (int i = 0; i < myins.size(); i++) {
                        myXML.addChildElement(myins.get(i).getXMLElement());
                    }


                } else if (type.equals(MSG.XML_ELEMENT_TYPE_GROUP)) {

                    Element user = myXML.getCildElement(MSG.XML_ELEMENT_USER);
                      user_id2 = user.getChild(MSG.XML_ELEMENT_USERS_ID).getText();

                    Date created_at = SQL.getInstansInGreenwich().getTime();
                    int conv_id = SQL.SQL_insert_into_conversation_title_name_conversation_photo_id_type_creator_id_created_at_updated_at(stat, title, name_conversation, 0, type, user_id, created_at);

                    SQL.SQL_insert_into_participants_conversation_id_users_id(stat, conv_id, user_id);
                    SQL.SQL_insert_into_participants_conversation_id_users_id(stat, conv_id, Integer.parseInt(user_id2));

                    conversation = SQL.SQL_select_conversation_all_from_conversation_where_conversation_id_users_id(stat, conv_id, user_id);

                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.addChildElement(conversation.getXMLElement());

                    ArrayList<Participants> myins = SQL.SQL_get_Array_participants_from_participants_where_conversation_id(stat, conv_id);
                    for (int i = 0; i < myins.size(); i++) {
                        myXML.addChildElement(myins.get(i).getXMLElement());
                    }

                }

                MyTask task = new MyTask(conversation, null, MyTask.SYSTEM_MSG);
                task.arg1 = Integer.parseInt(user_id2);
                QT.addTask(task);


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
