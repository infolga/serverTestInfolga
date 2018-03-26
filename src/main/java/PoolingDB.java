import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PoolingDB {

    private static final String url = "jdbc:mysql://test-db.ckhxvp0gw6hm.us-west-2.rds.amazonaws.com:3306/messenger?useUnicode=true&characterEncoding=utf8";
    private String user;
    private String password;
    private static PoolingDB poolingDB;

    private PoolingDB(String user, String password) {


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Done.");
            e.printStackTrace();
        }
        System.out.println("Done.");
        //
        System.out.println("Setting up data source.");
        DataSource dataSource = setupDataSource(url);
        System.out.println("Done.");
        //
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;

        try {
            System.out.println("Creating connection.");
            conn = dataSource.getConnection();
            System.out.println("Creating statement.");
            stmt = conn.createStatement();
            System.out.println("Executing statement.");
            rset = stmt.executeQuery("SHOW DATABASES;");
            System.out.println("Results:");
            int numcols = rset.getMetaData().getColumnCount();
            while (rset.next()) {
                for (int i = 1; i <= numcols; i++) {
                    System.out.print("\t" + rset.getString(i));
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
            } catch (Exception e) {
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
            }
        }
    }

    public static PoolingDB instanse() {
        if (poolingDB == null) {
            poolingDB = new PoolingDB();
        }
        return poolingDB;
    }

    private DataSource setupDataSource(String connectURI) {

        ConnectionFactory connectionFactory =
            new DriverManagerConnectionFactory(connectURI, user, password);

        PoolableConnectionFactory poolableConnectionFactory =
            new PoolableConnectionFactory(connectionFactory, null);

        ObjectPool<PoolableConnection> connectionPool =
            new GenericObjectPool<>(poolableConnectionFactory);

        poolableConnectionFactory.setPool(connectionPool);

        PoolingDataSource<PoolableConnection> dataSource =
            new PoolingDataSource<>(connectionPool);

        return dataSource;
    }
}