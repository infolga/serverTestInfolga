import io.netty.channel.ChannelHandlerContext;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Runnable_USER_LOGIN implements Runnable {

    private MyXML myXMLParser;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;
    private ResultSet res;

    public Runnable_USER_LOGIN(MyXML myXMLParser, ChannelHandlerContext ctx, PoolingDB DB) {
        this.myXMLParser = myXMLParser;
        this.ctx = ctx;
        this.db = DB;
    }

    @Override
    public void run() {

        try {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            String phone = myXMLParser.getValueInActionsXML(MSG.XML_ELEMENT_PHONE);
            String pass = myXMLParser.getValueInActionsXML(MSG.XML_ELEMENT_PASSWORD);

            String device_info = myXMLParser.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_INFO);
            String device_token = myXMLParser.getValueInActionsXML(MSG.XML_ELEMENT_DRVISE_TOKEN);

            String sql = MyProperties.instans().getProperty("SQL_get_user_from_phone_pass", "66 ");

            String sql_exe = String.format(sql, phone, pass);


            con = db.getConnection();
            stat = con.createStatement();
            res = stat.executeQuery(sql_exe);

            int size = 0;

            res.last();
            size = res.getRow();
            res.first();

            if (size == 1) {

                int user_id = res.getInt("id");
                System.out.println("user_id " + user_id);
                int i = addDeviseToUsers(user_id, device_info, device_token);

                String token = getToken(user_id, i);
                if (token == null) {
                    token = createNewToken(user_id, i);
                }
                System.out.println("yyyyyyyyraaaaaaaaaaa");

            } else {
                System.out.println("FAIL");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            System.out.println("333");
        }


    }

    private int addDeviseToUsers(int user_id, String device_info, String device_token) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_insert_devises", "66 ");
        System.out.println("addDeviseToUsers ");
        String sql_exe = String.format(sql, user_id, device_info, device_token);
        try {
            stat.executeUpdate(sql_exe);
        }
        catch (SQLIntegrityConstraintViolationException o){

            System.out.println("Duplicate id" );
        }


        sql = MyProperties.instans().getProperty("SQL_get_devise_id_from_device_token", "66 ");
        sql_exe = String.format(sql, device_token);
        System.out.println( sql_exe);
        ResultSet R = stat.executeQuery(sql_exe);

        R.first();
        int id = R.getInt("id");
        System.out.println("addDeviseToUsers id" + id);
        R.close();
        return id;

    }

    private String getToken(int user_id, int devices_id) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_get_token_from_users_id", "66 ");

        String sql_exe = String.format(sql, user_id, devices_id);
        System.out.println("getToken id");
        ResultSet R = stat.executeQuery(sql_exe);
        int size = 0;

        R.last();
        size = R.getRow();
        R.first();
        if (size > 0) {
            Date date = new java.util.Date();
            System.out.println(new java.util.Date().toString());

            Date valid_until = R.getDate("valid_until");
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

    private String createNewToken(int user_id, int devices_id) throws SQLException {
        System.out.println("createNewToken ");


        DateFormat df = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");

        Date date = new java.util.Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 30);// прибавляем 3 дня к установленной дате
        Date newDate = instance.getTime();

        String str = "" + user_id + date.toString() + devices_id + newDate.toString();

        byte[] encodedBytes = Base64.getEncoder().encode(str.getBytes());
        String token = new String(encodedBytes);

        String sql = MyProperties.instans().getProperty("SQL_insert_token", "66 ");
        String sql_exe = String.format(sql, user_id, token,  df.format(date), devices_id, df.format(newDate));

        System.out.println(sql_exe);
        stat.executeUpdate(sql_exe);

        return token;

    }

}
