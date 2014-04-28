package h2;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility Class for H2 related helper methods
 * Created by mcmoe on 4/26/2014.
 */
public class H2Server {
    static Connection createInMemoryH2Connection() throws SQLException {
        return createH2Connection(DbInfo.H2_MEM_DB);
    }

    static Connection createEmbeddedH2Connection() throws SQLException {
        return createH2Connection(DbInfo.H2_EMBD_DB);
    }

    private static Connection createH2Connection(String mode) throws SQLException {
        return JdbcConnectionPool.create(mode, DbInfo.USER, DbInfo.PASSWORD).getConnection();
    }

    static Statement createStatement(Connection connection) throws SQLException {
        return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }
}
