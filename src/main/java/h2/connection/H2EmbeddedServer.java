package h2.connection;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * H2 Embedded DB Server with pool managed connections
 * Created by mcmoe on 4/28/2014.
 */
public class H2EmbeddedServer {

    private final JdbcConnectionPool pool;

    public H2EmbeddedServer() {
        pool = JdbcConnectionPool.create(DbInfo.H2_EMBD_DB, DbInfo.USER, DbInfo.PASSWORD);
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void close() {
        pool.dispose();
    }
}
