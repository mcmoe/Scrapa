package h2.connection;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * H2 In Memory DB Server with pool managed connections
 * Created by mcmoe on 4/26/2014.
 */
public class H2MemoryServer {

    private final JdbcConnectionPool pool;

    public H2MemoryServer() {
        pool = JdbcConnectionPool.create(DbInfo.H2_MEM_DB, DbInfo.USER, DbInfo.PASSWORD);
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void close() {
        pool.dispose();
    }

}
