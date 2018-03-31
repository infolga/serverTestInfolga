import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Runnable_CONTACT_ADD implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    public Runnable_CONTACT_ADD(MyXML myXML, ChannelHandlerContext ctx, PoolingDB db) throws SQLException {
        this.myXML = myXML;
        this.ctx = ctx;
        this.db = db;
        con = db.getConnection();

    }

    @Override
    public void run() {
        try {

            Log.info("Runnable_CONTACT_ADD");

            con.setAutoCommit(false);
            stat = con.createStatement();

            String token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);
            int user_id = SQL.SQL_select_users_id_from_access_where_token(stat, token);

            if (user_id != -1) {// токен найден и рабочий
                String phone = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PHONE);
                String first_name = myXML.getValueInActionsXML(MSG.XML_ELEMENT_FIRST_NAME);
                String last_name = myXML.getValueInActionsXML(MSG.XML_ELEMENT_LAST_NAME);

                int contacts_id = SQL.SQL_select_contacts_id_from_contacts_where_phone(stat, phone);
                if (contacts_id == -1) {
                    SQL.SQL_insert_into_contacts_phone_email_first_name_last_name_created_at(stat, phone, "", first_name, last_name);
                    contacts_id = SQL.SQL_select_contacts_id_from_contacts_where_phone(stat, phone);
                }

                if (SQL.SQL_select_user_id_contact_id_from_user_contact_where_users_id_contact_id(stat, user_id, contacts_id) == -1) {
                    SQL.SQL_insert_into_user_contact_user_id_contact_id_first_name_last_name_created_at_updated_at(stat, user_id, contacts_id, first_name, last_name);
                }

                myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));


                myXML.addChild(MSG.XML_ELEMENT_CONTACT_ID, Integer.toString(contacts_id));


            } else {// токен недействительный
                myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_TOKEN_UNAVAILABLE));
                myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_TOKEN_UNAVAILABLE));
            }


            con.commit();
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
