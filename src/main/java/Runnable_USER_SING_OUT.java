import io.netty.channel.ChannelHandlerContext;
import org.mortbay.log.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Runnable_USER_SING_OUT implements Runnable {

    private MyXML myXML;
    private ChannelHandlerContext ctx;
    private PoolingDB db;
    private Connection con;
    private Statement stat;

    private QueueTask QT;


    public Runnable_USER_SING_OUT(MyXML myXML, ChannelHandlerContext ctx, PoolingDB db, QueueTask Q) throws SQLException {
        this.myXML = myXML;
        this.ctx = ctx;
        this.db = db;
        con = db.getConnection();
        QT = Q;
    }

    @Override
    public void run() {

        try {
            Log.info("Runnable_USER_SING_OUT");
            con.setAutoCommit(false);
            stat = con.createStatement();

            String token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);
            int user_id = SQL.SQL_select_users_id_from_access_where_token(stat, token);

            if (user_id != -1) {// токен найден и рабочий
                SQL.SQL_update_into_access(stat, token);

            } else {//пользователь не найден


            }
            con.commit();


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



