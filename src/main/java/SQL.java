import org.apache.commons.codec.binary.Base64;
import org.mortbay.log.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SQL {


    static {
        (calendar = Calendar.getInstance()).setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

    }

    public final static Calendar calendar;


    public static final DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

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


    public static int SQL_insert_into_users_phone_email_password_first_name_last_name_user_name_created_at_(Statement stat, String phone, String email, String password, String first_name, String last_name, String user_name) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_insert_into_users_phone_email_password_first_name_last_name_user_name_created_at_", "66 ");
        Date created_at = new java.util.Date();

        Calendar instance = Calendar.getInstance();
        instance.setTime(created_at); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.YEAR, -1);//
        Date valid_ = instance.getTime();

        String sql_exe = String.format(sql, phone, email, password, first_name, last_name, user_name, df.format(created_at), df.format(valid_), df.format(created_at));

        stat.executeUpdate(sql_exe);
        return SQL_select_LAST_INSERT_ID(stat);

    }


    public static int SQL_insert_into_participants_conversation_id_users_id(Statement stat, int conversation_id, int users_id) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_insert_into_participants_conversation_id_users_id", "66 ");

        Date created_at = new java.util.Date();

        String sql_exe = String.format(sql, conversation_id, users_id, df.format(created_at));
        stat.executeUpdate(sql_exe);
        return SQL_select_LAST_INSERT_ID(stat);
    }


    public static int SQL_insert_into_contacts_phone_email_first_name_last_name_created_at(Statement stat, String phone, String email, String first_name, String last_name) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_insert_into_contacts_phone_email_first_name_last_name_created_at", "66 ");

        Date created_at = new java.util.Date();

        String sql_exe = String.format(sql, phone, email, first_name, last_name, df.format(created_at));
        System.out.println(sql_exe);
        stat.executeUpdate(sql_exe);
        return SQL_select_LAST_INSERT_ID(stat);
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


    public static Conversation SQL_select_conversation_all_from_conversation_where_conversation_id_users_id(Statement stat, int conversation_id, int user_id) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_select_conversation_all_from_conversation_where_conversation_id_users_id", "66 ");
        String sql_exe = String.format(sql, conversation_id, user_id);
        ResultSet R = stat.executeQuery(sql_exe);
        Conversation conversation = new Conversation();

        R.first();
        conversation.setConversation_id(R.getInt("con_id"));
        conversation.setTitle(R.getString("title"));
        conversation.setPhoto_id(R.getInt("photo_id"));
        conversation.setCreator_id(R.getInt("creator_id"));
        conversation.setCreated_at(R.getString("created_at"));
        conversation.setTime_last_viev(R.getString("last_view"));
        conversation.setType(R.getString("type"));
        conversation.setName_conversation(R.getString("name_conversation"));
        R.close();
        return conversation;


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

    public static int SQL_insert_into_devices_users_id_device_info_device_token(Statement stat, int user_id, String device_info, String device_token) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_insert_into_devices_users_id_device_info_device_token", "66 ");
        //System.out.println("addDeviseToUsers ");
        String sql_exe = String.format(sql, user_id, device_info, device_token);

        stat.executeUpdate(sql_exe);
        return SQL_select_LAST_INSERT_ID(stat);

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


    public static int SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until(Statement stat, int user_id, int devices_id) throws SQLException {
        SQL_set_time_zone(stat);
        // System.out.println("SQL_insert_new_token ");
        Date created_at = new java.util.Date();

        Calendar instance = Calendar.getInstance();
        instance.setTime(created_at); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, 30);// прибавляем 3 дня к установленной дате
        Date valid_until = instance.getTime();

        String str = "" + user_id + created_at.toString() + devices_id + valid_until.toString();
        byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
        // byte[] encodedBytes = Base64.getEncoder().encode(str.getBytes());
        String token = new String(encodedBytes);//генерация токена

        String sql = MyProperties.instans().getProperty("SQL_insert_into_access_users_id_token_created_at_devices_id_valid_until", "66 ");
        String sql_exe = String.format(sql, user_id, token, df.format(created_at), devices_id, df.format(valid_until));

        //System.out.println(sql_exe);

        stat.executeUpdate(sql_exe);
        return SQL_select_LAST_INSERT_ID(stat);
    }


//    public static int SQL_select_conversation_id_from_conversation_conversation_title_name_conversation_photo_id_type_creator_id_created_at_updated_at(Statement stat, String title, String name_conversation, int photo_id, String type, int creator_id, Date created_at) throws SQLException {
//        SQL_set_time_zone(stat);
//        // System.out.println("SQL_insert_new_token ");
//
//        Calendar instance = Calendar.getInstance();
//
//        String sql = MyProperties.instans().getProperty("SQL_select_conversation_id_from_conversation_conversation_title_name_conversation_photo_id_type_creator_id_created_at_updated_at", "66 ");
//        String sql_exe = String.format(sql, title, name_conversation, photo_id, type, creator_id, df.format(created_at), df.format(created_at));
//
//        ResultSet R = stat.executeQuery(sql_exe);
//        return Return_id(R, "id");
//
//    }


    public static int SQL_insert_into_conversation_title_name_conversation_photo_id_type_creator_id_created_at_updated_at(Statement stat, String title, String name_conversation, int photo_id, String type, int creator_id, Date created_at) throws SQLException {
        SQL_set_time_zone(stat);
        // System.out.println("SQL_insert_new_token ");

        Calendar instance = Calendar.getInstance();

        String sql = MyProperties.instans().getProperty("SQL_insert_into_conversation_title_name_conversation_photo_id_type_creator_id_created_at_updated_at", "66 ");
        String sql_exe = String.format(sql, title, name_conversation, photo_id, type, creator_id, df.format(created_at), df.format(created_at));

        //System.out.println(sql_exe);
        stat.executeUpdate(sql_exe);
        return SQL_select_LAST_INSERT_ID(stat);

    }


    public static int SQL_insert_into_user_contact_user_id_contact_id_first_name_last_name_created_at_updated_at(Statement stat, int user_id, int contact_id, String first_name, String last_name) throws SQLException {
        SQL_set_time_zone(stat);
        // System.out.println("SQL_insert_new_token ");
        Date created_at = new java.util.Date();

        Calendar instance = Calendar.getInstance();

        String sql = MyProperties.instans().getProperty("SQL_insert_into_user_contact_user_id_contact_id_first_name_last_name_created_at_updated_at", "66 ");
        String sql_exe = String.format(sql, user_id, contact_id, first_name, last_name, df.format(created_at), df.format(created_at));

        //System.out.println(sql_exe);
        stat.executeUpdate(sql_exe);
        return SQL_select_LAST_INSERT_ID(stat);


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


    public static int SQL_get_common_conversation_id_single_where_users_id1_and_users_id2(Statement stat, int user_id, int user_id2) throws SQLException {
        SQL_set_time_zone(stat);
        String sql = MyProperties.instans().getProperty("SQL_get_common_conversation_id_single_where_users_id1_and_users_id2", "66 ");
        // System.out.println("getDeviseIdToUsers ");
        String sql_exe = String.format(sql, user_id, user_id2);
        System.out.println(sql_exe);

        ResultSet R = stat.executeQuery(sql_exe);
        return Return_id(R, "id");


    }


    private static void SQL_set_time_zone(Statement stat) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_set_time_zone", "66 ");
        stat.execute(sql);

    }

    public static ArrayList<User> SQL_get_Array_users_from_users_like(Statement stat, String user_name_like, int afte, int before) throws SQLException {
        SQL_set_time_zone(stat);

        String sql = MyProperties.instans().getProperty("SQL_get_Array_users_from_users_like", "66 ");
        String sql_exe = String.format(sql, "%" + user_name_like + "%", Integer.toString(afte), Integer.toString(before - afte + 1));
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


    public static ArrayList<Myin> SQL_select_first_15_participants_and_user_in_each_conversation_where_user_id(Statement stat, int user_id) throws SQLException {
        SQL_set_time_zone(stat);

        String sql = MyProperties.instans().getProperty("SQL_select_first_15_participants_in_each_conversation_where_user_id", "66 ");
        String sql_exe = String.format(sql, user_id);
        Log.info(sql_exe);
        ResultSet R = stat.executeQuery(sql_exe);

        ArrayList<Myin> arrayusers = new ArrayList<>();
        while (R.next()) {
            Participants participants = new Participants();
            participants.setConversation_id(R.getInt("conversation_id"));
            participants.setId_participants(R.getInt("id"));
            participants.setUsers_id(R.getInt("users_id"));
            arrayusers.add(participants);

            User user = new User();
            user.setUsers_id(R.getInt("us_id"));
            user.setPhone(R.getString("us_phone"));
            user.setEmail(R.getString("us_email"));
            user.setFirst_name(R.getString("us_first_name"));
            user.setLast_name(R.getString("us_last_name"));
            user.setUser_name(R.getString("us_user_name"));
            user.setIs_active(R.getInt("us_is_active"));

            user.setLast_online_at(R.getString("us_last_online_at"));
            arrayusers.add(user);
        }
        return arrayusers;
    }


    public static User SQL_get_users_from_users_where_id(Statement stat, int user_id) throws SQLException {
        SQL_set_time_zone(stat);

        String sql = MyProperties.instans().getProperty("SQL_select_all_from_users_where_users_id", "66 ");
        String sql_exe = String.format(sql, user_id);
        Log.info(sql_exe);
        ResultSet R = stat.executeQuery(sql_exe);


        R.next();
        User user = new User();
        user.setUsers_id(R.getInt("id"));
        user.setPhone(R.getString("phone"));
        user.setEmail(R.getString("email"));
        user.setFirst_name(R.getString("first_name"));
        user.setLast_name(R.getString("last_name"));
        user.setUser_name(R.getString("user_name"));
        user.setIs_active(R.getInt("is_active"));

        user.setLast_online_at(R.getString("last_online_at"));

        return user;
    }


    public static ArrayList<Myin> SQL_get_Array_сonversation_from_conversation(Statement stat, int user_id) throws SQLException {
        SQL_set_time_zone(stat);

        String sql = MyProperties.instans().getProperty("SQL_select_ALL_conversation", "66 ");
        String sql_exe = String.format(sql, user_id);
        Log.info(sql_exe);
        ResultSet R = stat.executeQuery(sql_exe);
        ArrayList<Myin> myinArrayList = new ArrayList<>();
        while (R.next()) {

            Conversation conversation = new Conversation();
            Messages messages = new Messages();
            conversation.setConversation_id(R.getInt("con_id"));
            conversation.setTitle(R.getString("con_title"));
            conversation.setName_conversation(R.getString("con_name_conversation"));
            conversation.setPhoto_id(R.getInt("con_photo_id"));
            conversation.setType(R.getString("con_type"));
            conversation.setCreated_at(R.getString("con_created_at"));
            conversation.setCreator_id(R.getInt("con_creator_id"));
            conversation.setTime_last_viev(R.getString("par_last_view"));
            myinArrayList.add(conversation);
            if (!conversation.getType().equals("single")) {

                Participants participants = new Participants();
                participants.setUsers_id(user_id);
                participants.setConversation_id(R.getInt("con_id"));
                participants.setId_participants(R.getInt("par_id"));
                myinArrayList.add(participants);
            }


            R.getInt("mes_id");
            if (!R.wasNull()) {
                messages.setConversation_id(R.getInt("con_id"));
                Log.info(Integer.toString(R.getInt("mes_id")));
                messages.setId(R.getInt("mes_id"));
                messages.setSender_id(R.getInt("mes_sender_id"));
                messages.setMessage_type(R.getString("mes_message_type"));
                messages.setMessage(R.getString("mes_message"));
                messages.setAttachment_thumb_url(R.getString("mes_attachment_thumb_url"));
                messages.setAttachment_url(R.getString("mes_attachment_url"));
                messages.setCreated_at(R.getString("mes_created_at"));

                myinArrayList.add(messages);
            }

        }

        R.close();
        return myinArrayList;


    }


    public static ArrayList<Myin> SQL_get_Array_participants_from_participants_where_conversation_id(Statement stat, int conversation_id) throws SQLException {
        SQL_set_time_zone(stat);

        String sql = MyProperties.instans().getProperty("SQL_get_Array_participants_from_participants_where_conversation_id", "66 ");
        String sql_exe = String.format(sql, conversation_id);
        Log.info(sql_exe);
        ResultSet R = stat.executeQuery(sql_exe);
        ArrayList<Myin> myinArrayList = new ArrayList<>();
        while (R.next()) {
            Participants participants = new Participants();
            participants.setUsers_id(R.getInt("users_id"));
            participants.setConversation_id(R.getInt("conversation_id"));
            participants.setId_participants(R.getInt("id"));
            myinArrayList.add(participants);
        }
        R.close();
        return myinArrayList;


    }


    private static int SQL_select_LAST_INSERT_ID(Statement stat) throws SQLException {
        String sql = MyProperties.instans().getProperty("SQL_select_LAST_INSERT_ID", "66 ");

        ResultSet R = stat.executeQuery(sql);
        R.first();
        int id = R.getInt("id");
        R.close();
        return id;
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
