package h2.connection;

import lombok.Cleanup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Unit test the H2 DB API
 * Created by mcmoe on 4/25/2014.
 */
public class H2Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2Test.class);

    @Test
    public void test_create_in_memory_h2() {
        try {
            @Cleanup Connection connection = H2Server.createInMemoryH2Connection();
            assertFalse(connection.isClosed());
        } catch(SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_create_embedded_h2() {
        try {
            @Cleanup Connection connection = H2Server.createEmbeddedH2Connection();
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }
}
