import io.netty.channel.ChannelHandlerContext;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            String phone = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PHONE);
            String password = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PASSWORD);

            String device_info = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_INFO);
            String device_token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_TOKEN);


            if (SQL_select_users_id_from_users_where_phone(phone)) {
                int user_id = SQL_select_users_id_from_users_where_phone_password(phone, password);
                if (user_id == -1) {//пароль неверный
                    myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                    myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD));
                    myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                    myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD));
                    System.out.println(myXML.toString());


                    System.out.println("пароль неверный");

                } else {//ура проходим авторизацию
                    int devices_id = SQL_select_devises_id_from_devices_where_users_id_device_token(user_id, device_token);
                    if (devices_id == -1) {
                        SQL_insert_into_devices_users_id_device_info_device_token(user_id, device_info, device_token);
                        devices_id = SQL_select_devises_id_from_devices_where_users_id_device_token(user_id, device_token);
                    }
                    String token = SQL_select_valid_token_from_access_where_users_id_devices_id(user_id, devices_id);
                    if (token == null) {
                        SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(user_id, devices_id);
                        token = SQL_select_valid_token_from_access_where_users_id_devices_id(user_id, devices_id);
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

    private int countRow(ResultSet R) throws SQLException {
        int size = 0;
        R.last();
        size = R.getRow();
        R.first();
        return size;
    }

    private int SQL_select_users_id_from_users_where_phone_password(String phone, String password) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_phone_password", "66 ");

        String sql_exe = String.format(sql, phone, password);
        ResultSet R = stat.executeQuery(sql_exe);

        return Return_id(R);
    }


    private boolean SQL_select_users_id_from_users_where_phone(String phone) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_phone", "66 ");
        String sql_exe = String.format(sql, phone);
        ResultSet R = stat.executeQuery(sql_exe);
        return countRow(R) == 1;

    }


    private int SQL_select_devises_id_from_devices_where_users_id_device_token(int user_id, String device_token) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_devises_id_from_devices_where_users_id_device_token", "66 ");
        System.out.println("getDeviseIdToUsers ");
        String sql_exe = String.format(sql, user_id, device_token);
        ResultSet R = stat.executeQuery(sql_exe);
        return Return_id(R);


    }

    private void SQL_insert_into_devices_users_id_device_info_device_token(int user_id, String device_info, String device_token) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_insert_into_devices_users_id_device_info_device_token", "66 ");
        System.out.println("addDeviseToUsers ");
        String sql_exe = String.format(sql, user_id, device_info, device_token);
        try {
            stat.executeUpdate(sql_exe);
        } catch (SQLIntegrityConstraintViolationException o) {

            System.out.println("Duplicate id");
        }
    }

    private String SQL_select_valid_token_from_access_where_users_id_devices_id(int user_id, int devices_id) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_valid_token_from_access_where_users_id_devices_id", "66 ");
        String sql_exe = String.format(sql, user_id, devices_id);
        System.out.println("getToken id");
        ResultSet R = stat.executeQuery(sql_exe);

        if (countRow(R) > 0) {

            Date date = new java.util.Date();
            Date valid_until = R.getTimestamp("valid_until");
            if (date.getTime() > valid_until.getTime()) {
                R.close();
                return null;
            } else {
                String token = R.getString("token");
                R.close();
                return token;
            }

        } else {
            R.close();
            return null;
        }
    }


    private void SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(int user_id, int devices_id) throws SQLException {
        System.out.println("SQL_insert_new_token ");


        DateFormat df = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");

        Date created_at = new java.util.Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(created_at); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 30);// прибавляем 3 дня к установленной дате
        Date valid_until = instance.getTime();

        String str = "" + user_id + created_at.toString() + devices_id + valid_until.toString();

        byte[] encodedBytes = Base64.getEncoder().encode(str.getBytes());
        String token = new String(encodedBytes);

        String sql = MyProperties.instans().getProperty("SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until", "66 ");
        String sql_exe = String.format(sql, user_id, token, df.format(created_at), devices_id, df.format(valid_until));

        System.out.println(sql_exe);
        stat.executeUpdate(sql_exe);


    }


    private int Return_id(ResultSet r) throws SQLException {
        if (countRow(r) == 1) {
            int devises_id = r.getInt("id");
            r.close();
            return devises_id;
        } else {
            r.close();
            return -1;
        }
    }


}
