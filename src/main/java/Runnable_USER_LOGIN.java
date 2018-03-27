import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;
import java.util.Locale;

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
        stat = con.createStatement();
    }

    @Override
    public void run() {

        try {

            String phone = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PHONE);
            String password = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PASSWORD);

            String device_info = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_INFO);
            String device_token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_TOKEN);


            if (SQL.SQL_select_users_id_from_users_where_phone(stat, phone)==1) {
                int user_id = SQL.SQL_select_users_id_from_users_where_phone_password(stat,phone, password);
                if (user_id == -1) {//пароль неверный
                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD));
                    System.out.println(myXML.toString());


                    System.out.println("пароль неверный");

                } else {//ура проходим авторизацию
                    int devices_id = SQL.SQL_select_devises_id_from_devices_where_users_id_device_token(stat, user_id, device_token);
                    if (devices_id == -1) {
                        SQL.SQL_insert_into_devices_users_id_device_info_device_token(stat, user_id, device_info, device_token);
                        devices_id = SQL.SQL_select_devises_id_from_devices_where_users_id_device_token(stat, user_id, device_token);
                    }
                    String token = SQL.SQL_select_valid_token_from_access_where_users_id_devices_id(stat, user_id, devices_id);
                    if (token == null) {
                        SQL.SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(stat, user_id, devices_id);
                        token = SQL.SQL_select_valid_token_from_access_where_users_id_devices_id(stat, user_id, devices_id);
                    }
                    System.out.println("token = " + token);

                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));

                    String sql = MyProperties.instans().getProperty("SQL_select_all_from_users_where_users_id", "66 ");

                    String sql_exe = String.format(sql, user_id);
                    ResultSet R = stat.executeQuery(sql_exe);
                    System.out.println(sql_exe);
                    R.first();
                    myXML.addChild(MSG.XML_ELEMENT_EMAIL, R.getString(MSG.XML_ELEMENT_EMAIL));
                    myXML.addChild(MSG.XML_ELEMENT_FIRST_NAME, R.getString(MSG.XML_ELEMENT_FIRST_NAME));
                    myXML.addChild(MSG.XML_ELEMENT_LAST_NAME, R.getString(MSG.XML_ELEMENT_LAST_NAME));
                    myXML.addChild(MSG.XML_ELEMENT_USER_NAME, R.getString(MSG.XML_ELEMENT_USER_NAME));
                    myXML.addChild(MSG.XML_ELEMENT_USERS_ID, Integer.toString(user_id));
                    myXML.addChild(MSG.XML_ELEMENT_TOKEN, token);
                    System.out.println("успешно");
                    System.out.println(myXML.toString());
                    R.close();
                }

            } else {//пользователь не найден
                myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_PHONE_NOT_FOUND));
                myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_PHONE_NOT_FOUND));
                System.out.println("пользователь не найден");
                System.out.println(myXML.toString());

            }
            ctx.write(myXML.toString());
            ctx.flush();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }


}
