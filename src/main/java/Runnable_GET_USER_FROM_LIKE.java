import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Runnable_GET_USER_FROM_LIKE implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    private QueueTask QT;


    public Runnable_GET_USER_FROM_LIKE(MyXML myXML, ChannelHandlerContext ctx, PoolingDB db, QueueTask Q) throws SQLException {
        this.myXML = myXML;
        this.ctx = ctx;
        this.db = db;
        con = db.getConnection();
        QT = Q;
    }

    @Override
    public void run() {

        try {


            Log.info("Runnable_GET_USER_FROM_LIKE");

           // Log.info(myXML.toString());

            con.setAutoCommit(false);
            stat = con.createStatement();

            String token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);
            int user_id = SQL.SQL_select_users_id_from_access_where_token(stat, token);

            if (user_id != -1) {// токен найден и рабочий

                String user_name_from_like = myXML.getValueInActionsXML(MSG.XML_ELEMENT_USER_NAME_LIKE);
                String afte = myXML.getValueInActionsXML(MSG.XML_ELEMENT_AFTER);
                String before = myXML.getValueInActionsXML(MSG.XML_ELEMENT_BEFORE);

                ArrayList<User> users = SQL.SQL_get_Array_users_from_users_like(stat, user_name_from_like, Integer.parseInt(afte), Integer.parseInt(before));


                myXML.setNameRoot(MSG.XML_TYPE_RESPONSE);
                myXML.setAttributeRoot(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));
                myXML.jumpToChildFromRoot(MSG.XML_ELEMENT_ACTIONS);
                myXML.setAtribute(MSG.XML_ATRIBUT_RESULT, Integer.toString(MSG.XML_RESULT_VALUES_OK));


                for (int i = 0; i < users.size(); i++) {
                    myXML.addChildElement(users.get(i).getXMLElement());
                }
              //  Log.info(myXML.toString());


            } else {
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



