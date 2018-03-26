import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PoolingDB {

    private String url;
    private String user;
    private String password;
    private DataSource dataSource;

    public PoolingDB(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void Connect() {
            dataSource = setupDataSource(url, user, password);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    private DataSource setupDataSource(String connectURI, String us, String pas) {

        ConnectionFactory connectionFactory =
            new DriverManagerConnectionFactory(connectURI, us, pas);

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