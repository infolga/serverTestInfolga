import org.mortbay.log.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SQL {


    static {
        (calendar = Calendar.getInstance()).setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

    }

    public final static Calendar calendar;


    public static final DateFormat df = new SimpleDateFormat("YYYY-MM-dd kk:mm:ss");

    public static int countRow(ResultSet R) throws SQLException {
        int size = 0;
        R.last();
        size = R.getRow();
        R.first();
        return size;
    }

    public static int SQL_select_users_id_from_users_where_phone_password(Statement stat, String phone, String password) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_phone_password", "66 ");

        String sql_exe = String.format(sql, phone, password);
        ResultSet R = stat.executeQuery(sql_exe);

        return Return_id(R, "id");
    }


    public static int SQL_select_users_id_from_access_where_token(Statement stat, String token) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_access_where_token", "66 ");

        String sql_exe = String.format(sql, token);
        ResultSet R = stat.executeQuery(sql_exe);

        return Return_id(R, "users_id");
    }


    public static int SQL_select_users_id_from_users_where_user_name(Statement stat, String user_name) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_user_name", "66 ");
        String sql_exe = String.format(sql, user_name);
        ResultSet R = stat.executeQuery(sql_exe);
        return countRow(R);

    }


    public static void SQL_insert_into_users_phone_email_password_first_name_last_name_user_name_created_at_(Statement stat, String phone, String email, String password, String first_name, String last_name, String user_name) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_insert_into_users_phone_email_password_first_name_last_name_user_name_created_at_", "66 ");
        Date created_at = new java.util.Date();

        Calendar instance = Calendar.getInstance();
        instance.setTime(created_at); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.YEAR, -1);//
        Date valid_ = instance.getTime();

        String sql_exe = String.format(sql, phone, email, password, first_name, last_name, user_name, df.format(created_at), df.format(valid_), df.format(created_at));

        stat.executeUpdate(sql_exe);


    }

    public static void SQL_insert_into_contacts_phone_email_first_name_last_name_created_at(Statement stat, String phone, String email, String first_name, String last_name) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_insert_into_contacts_phone_email_first_name_last_name_created_at", "66 ");

        Date created_at = new java.util.Date();

        String sql_exe = String.format(sql, phone, email, first_name, last_name, df.format(created_at));
        System.out.println(sql_exe);
        stat.executeUpdate(sql_exe);
    }


    public static void SQL_update_contacts_set_email_first_name_last_name_where_contacts_id(Statement stat, String email, String first_name, String last_name, int contacts_id) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_update_contacts_set_email_first_name_last_name_where_contacts_id", "66 ");

        String sql_exe = String.format(sql, email, first_name, last_name, contacts_id);

        stat.executeUpdate(sql_exe);
    }


    public static int SQL_select_users_id_from_users_where_phone(Statement stat, String phone) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_phone", "66 ");
        String sql_exe = String.format(sql, phone);
        ResultSet R = stat.executeQuery(sql_exe);
        return countRow(R);

    }


    public static int SQL_select_contacts_id_from_contacts_where_phone(Statement stat, String phone) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_contacts_id_from_contacts_where_phone", "66 ");
        String sql_exe = String.format(sql, phone);
        ResultSet R = stat.executeQuery(sql_exe);
        return Return_id(R, "id");


    }


    public static int SQL_select_devises_id_from_devices_where_users_id_device_token(Statement stat, int user_id, String device_token) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_devises_id_from_devices_where_users_id_device_token", "66 ");
        // System.out.println("getDeviseIdToUsers ");
        String sql_exe = String.format(sql, user_id, device_token);
        ResultSet R = stat.executeQuery(sql_exe);
        return Return_id(R, "id");


    }

    public static void SQL_insert_into_devices_users_id_device_info_device_token(Statement stat, int user_id, String device_info, String device_token) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_insert_into_devices_users_id_device_info_device_token", "66 ");
        //System.out.println("addDeviseToUsers ");
        String sql_exe = String.format(sql, user_id, device_info, device_token);

        stat.executeUpdate(sql_exe);

    }


    public static String SQL_select_valid_token_from_access_where_users_id_devices_id(Statement stat, int user_id, int devices_id) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_valid_token_from_access_where_users_id_devices_id", "66 ");
        String sql_exe = String.format(sql, user_id, devices_id);
        //System.out.println("getToken id");
        ResultSet R = stat.executeQuery(sql_exe);

        if (countRow(R) > 0) {

            Date date = new java.util.Date();
            Date valid_until = R.getTimestamp("valid_until", calendar);
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


    public static void SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(Statement stat, int user_id, int devices_id) throws SQLException {
        SQL_set_time_zone(stat);
        // System.out.println("SQL_insert_new_token ");
        Date created_at = new java.util.Date();

        Calendar instance = Calendar.getInstance();
        instance.setTime(created_at); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 30);// прибавляем 3 дня к установленной дате
        Date valid_until = instance.getTime();

        String str = "" + user_id + created_at.toString() + devices_id + valid_until.toString();

        byte[] encodedBytes = Base64.getEncoder().encode(str.getBytes());
        String token = new String(encodedBytes);//генерация токена

        String sql = MyProperties.instans().getProperty("SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until", "66 ");
        String sql_exe = String.format(sql, user_id, token, df.format(created_at), devices_id, df.format(valid_until));

        //System.out.println(sql_exe);

        stat.executeUpdate(sql_exe);
    }


    public static void SQL_insert_into_user_contact_user_id_contact_id_first_name_last_name_created_at_updated_at(Statement stat, int user_id, int contact_id, String first_name, String last_name) throws SQLException {
        SQL_set_time_zone(stat);
        // System.out.println("SQL_insert_new_token ");
        Date created_at = new java.util.Date();

        Calendar instance = Calendar.getInstance();

        String sql = MyProperties.instans().getProperty("SQL_insert_into_user_contact_user_id_contact_id_first_name_last_name_created_at_updated_at", "66 ");
        String sql_exe = String.format(sql, user_id, contact_id, first_name, last_name, df.format(created_at), df.format(created_at));

        //System.out.println(sql_exe);
        stat.executeUpdate(sql_exe);


    }

    public static int SQL_select_user_id_contact_id_from_user_contact_where_users_id_contact_id(Statement stat, int user_id, int contact_id) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_user_id_contact_id_from_user_contact_where_users_id_contact_id", "66 ");
        // System.out.println("getDeviseIdToUsers ");
        String sql_exe = String.format(sql, user_id, contact_id);
        System.out.println(sql_exe);

        ResultSet R = stat.executeQuery(sql_exe);
        return Return_id(R, "user_id");


    }


    private static void SQL_set_time_zone(Statement stat) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_set_time_zone", "66 ");
        stat.execute(sql);

    }

    public static ArrayList<User> SQL_get_Array_users_from_users_like(Statement stat, String user_name_like, int afte, int before) throws SQLException {
        SQL_set_time_zone(stat);

        String sql = MyProperties.instans().getProperty("SQL_get_Array_users_from_users_like", "66 ");
        String sql_exe = String.format(sql, "%"+user_name_like+"%", Integer.toString(afte), Integer.toString(before - afte+1));
        Log.info(sql_exe);
        ResultSet R = stat.executeQuery(sql_exe);

        ArrayList<User> arrayusers = new ArrayList<User>();
        while (R.next()) {
            User user = new User();
            user.setUsers_id(R.getInt("id"));
            user.setPhone(R.getString("phone"));
            user.setEmail(R.getString("email"));
            user.setFirst_name(R.getString("first_name"));
            user.setLast_name(R.getString("last_name"));
            user.setUser_name(R.getString("user_name"));
            user.setIs_active(R.getInt("is_active"));

            user.setLast_online_at(R.getString("last_online_at"));
            arrayusers.add(user);
        }
        return arrayusers;
    }


    private static int Return_id(ResultSet r, String columId) throws SQLException {
        if (countRow(r) == 1) {
            int devises_id = r.getInt(columId);
            r.close();
            return devises_id;
        } else {
            r.close();
            return -1;
        }
    }

}
