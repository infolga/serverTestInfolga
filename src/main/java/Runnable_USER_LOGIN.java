import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Runnable_USER_LOGIN implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    public Runnable_USER_LOGIN(MyXML myXMLParser, ChannelHandlerContext ctx, PoolingDB DB) throws SQLException {
        this.myXML = myXMLParser;
        this.ctx = ctx;
        this.db = DB;
        con = db.getConnection();
    }

    @Override
    public void run() {

        try {
            Log.info("Runnable_USER_LOGIN");
            con.setAutoCommit(false);
            stat = con.createStatement();
            String phone = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PHONE);
            String password = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PASSWORD);

            String device_info = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_INFO);
            String device_token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_TOKEN);

            //проверка номера в бд
            if (SQL.SQL_select_users_id_from_users_where_phone(stat, phone) == 1) {
                int user_id = SQL.SQL_select_users_id_from_users_where_phone_password(stat, phone, password);
                if (user_id == -1) {//пароль неверный
                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD));


                } else {//ура проходим авторизацию
                    int devices_id = SQL.SQL_select_devises_id_from_devices_where_users_id_device_token(stat, user_id, device_token);
                    //проверка наличия этого устройства в бд
                    if (devices_id == -1) {
                        devices_id=SQL.SQL_insert_into_devices_users_id_device_info_device_token(stat, user_id, device_info, device_token);
                       // devices_id = SQL.SQL_select_devises_id_from_devices_where_users_id_device_token(stat, user_id, device_token);
                    }
                    //поиск валидного токена
                    String token = SQL.SQL_select_valid_token_from_access_where_users_id_devices_id(stat, user_id, devices_id);
                    if (token == null) {
                        SQL.SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(stat, user_id, devices_id);
                        token = SQL.SQL_select_valid_token_from_access_where_users_id_devices_id(stat, user_id, devices_id);
                    }
                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));

                    User user = SQL.SQL_get_users_from_users_where_id(stat, user_id);
                    myXML.addChild(MSG.XML_ELEMENT_TOKEN, token);

                    myXML.addChildElement(user.getXMLElement());
                }

            } else {//пользователь не найден
                myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_PHONE_NOT_FOUND));
                myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_PHONE_NOT_FOUND));


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



