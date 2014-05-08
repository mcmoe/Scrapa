package h2.flyway;

import h2.connection.H2EmbeddedServer;
import h2.connection.H2Utils;
import lombok.Cleanup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test flyway migration results
 * This will help identify a missing gradle flywayMigrate run
 * Created by mcmoe on 5/8/2014.
 */
public class FlywayMigrationsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlywayMigrationsTest.class);
    private static final String CHECK_SCRAPA_DATA_TABLE_EXISTENCE = "SELECT * from INFORMATION_SCHEMA.TABLES where TABLE_NAME = \'SCRAPA_DATA\'";
    private static final int TABLE_NAME_COLUMN = 3;

    @Test
    public void verify_table_scrapa_data_existence() {
        try {
            @Cleanup H2EmbeddedServer h2EmbeddedServer = new H2EmbeddedServer();
            @Cleanup Connection connection = h2EmbeddedServer.getConnection();
            java.sql.Statement statement = H2Utils.createStatement(connection);

            ResultSet resultSet = statement.executeQuery(CHECK_SCRAPA_DATA_TABLE_EXISTENCE);
            assertEquals(true, resultSet.next());
            assertEquals("SCRAPA_DATA", resultSet.getString(TABLE_NAME_COLUMN));
            assertEquals(false, resultSet.next());

        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }
}
