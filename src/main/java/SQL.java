import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class SQL {


    public static int countRow(ResultSet R) throws SQLException {
        int size = 0;
        R.last();
        size = R.getRow();
        R.first();
        return size;
    }

    public static int SQL_select_users_id_from_users_where_phone_password(Statement stat, String phone, String password) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_phone_password", "66 ");

        String sql_exe = String.format(sql, phone, password);
        ResultSet R = stat.executeQuery(sql_exe);

        return Return_id(R);
    }





    public static int SQL_select_users_id_from_users_where_user_name(Statement stat,String user_name) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_user_name", "66 ");
        String sql_exe = String.format(sql, user_name);
        ResultSet R = stat.executeQuery(sql_exe);
        return countRow(R) ;

    }




    public static void SQL_insert_into_users_phone_email_password_first_name_last_name_user_name_created_at_(Statement stat,String phone,String email,String password,String first_name,String last_name,String user_name){

        String sql = MyProperties.instans().getProperty("SQL_insert_into_users_phone_email_password_first_name_last_name_user_name_created_at_", "66 ");

        DateFormat df = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        Date created_at = new java.util.Date();

        String sql_exe = String.format(sql, phone, email, password,first_name,last_name,user_name,df.format(created_at));
        try {
            stat.executeUpdate(sql_exe);
        } catch (SQLIntegrityConstraintViolationException o) {

            System.out.println("Duplicate id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int SQL_select_users_id_from_users_where_phone(Statement stat,String phone) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_users_id_from_users_where_phone", "66 ");
        String sql_exe = String.format(sql, phone);
        ResultSet R = stat.executeQuery(sql_exe);
        return countRow(R) ;

    }


    public static int SQL_select_devises_id_from_devices_where_users_id_device_token(Statement stat,int user_id, String device_token) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_devises_id_from_devices_where_users_id_device_token", "66 ");
        System.out.println("getDeviseIdToUsers ");
        String sql_exe = String.format(sql, user_id, device_token);
        ResultSet R = stat.executeQuery(sql_exe);
        return Return_id(R);


    }

    public static void SQL_insert_into_devices_users_id_device_info_device_token(Statement stat,int user_id, String device_info, String device_token) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_insert_into_devices_users_id_device_info_device_token", "66 ");
        System.out.println("addDeviseToUsers ");
        String sql_exe = String.format(sql, user_id, device_info, device_token);
        try {
            stat.executeUpdate(sql_exe);
        } catch (SQLIntegrityConstraintViolationException o) {

            System.out.println("Duplicate id");
        }
    }

    public static String SQL_select_valid_token_from_access_where_users_id_devices_id(Statement stat,int user_id, int devices_id) throws SQLException {
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


    public static void SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(Statement stat,int user_id, int devices_id) throws SQLException {
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


    private static int Return_id(ResultSet r) throws SQLException {
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
