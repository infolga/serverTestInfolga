import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Runnable_USER_REG implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    public Runnable_USER_REG(MyXML myXML, ChannelHandlerContext ctx, PoolingDB db) throws SQLException {
        this.myXML = myXML;
        this.ctx = ctx;
        this.db = db;
        con = db.getConnection();

    }

    @Override
    public void run() {
        try {
            con.setAutoCommit(false);
            stat = con.createStatement();
            Log.info("Runnable_USER_REG");
            String phone = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PHONE);
            String password = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PASSWORD);
            String user_name = myXML.getValueInActionsXML(MSG.XML_ELEMENT_USER_NAME);
            String first_name = myXML.getValueInActionsXML(MSG.XML_ELEMENT_FIRST_NAME);
            String last_name = myXML.getValueInActionsXML(MSG.XML_ELEMENT_LAST_NAME);
            String email = myXML.getValueInActionsXML(MSG.XML_ELEMENT_EMAIL);

            String device_info = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_INFO);
            String device_token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_TOKEN);


            if (SQL.SQL_select_users_id_from_users_where_phone(stat, phone) == 0) {

                if (SQL.SQL_select_users_id_from_users_where_user_name(stat, user_name) == 0) {// регистрация возможна

                    SQL.SQL_insert_into_users_phone_email_password_first_name_last_name_user_name_created_at_(stat, phone, email, password, first_name, last_name, user_name);
                    int user_id = SQL.SQL_select_users_id_from_users_where_phone_password(stat, phone, password);
                    int contacts_id = SQL.SQL_select_contacts_id_from_contacts_where_phone(stat, phone);
                    if (contacts_id == -1) {
                        SQL.SQL_insert_into_contacts_phone_email_first_name_last_name_created_at(stat, phone, email, first_name, last_name);
                    } else {
                        SQL.SQL_update_contacts_set_email_first_name_last_name_where_contacts_id(stat, email, first_name, last_name, contacts_id);
                    }


//                    SQL.SQL_insert_into_devices_users_id_device_info_device_token(stat, user_id, device_info, device_token);
//                    int devices_id = SQL.SQL_select_devises_id_from_devices_where_users_id_device_token(stat, user_id, device_token);
//                    SQL.SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(stat, user_id, devices_id);
//                    String token = SQL.SQL_select_valid_token_from_access_where_users_id_devices_id(stat, user_id, devices_id);


                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    // myXML.addChild(MSG.XML_ELEMENT_TOKEN, token);
                } else {//такой user_name занят
                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_USER_NAME_UNAVAILABLE));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_USER_NAME_UNAVAILABLE));

                }

            } else {// такой телефон занят
                myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_PHONE_UNAVAILABLE));
                myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_PHONE_UNAVAILABLE));
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
