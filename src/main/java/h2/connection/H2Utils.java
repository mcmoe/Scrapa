package h2.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for H2 related helper methods
 * Created by mcmoe on 4/28/2014.
 */
public class H2Utils {
    public static Statement createStatement(Connection connection) throws SQLException {
        return connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
    }
}
