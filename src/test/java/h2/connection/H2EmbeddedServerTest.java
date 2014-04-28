package h2.connection;

import lombok.Cleanup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Unit test the H2 DB Embedded mode Server
 * Created by mcmoe on 4/28/2014.
 */
public class H2EmbeddedServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2EmbeddedServerTest.class);

    @Test
    public void test_create_embedded_h2() {
        try {
            @Cleanup H2EmbeddedServer h2EmbeddedServer = new H2EmbeddedServer();
            @Cleanup Connection connection = h2EmbeddedServer.getConnection();
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }
}
