package h2.connection;

import lombok.Cleanup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Unit test the H2 DB In-Memory mode Server
 * Created by mcmoe on 4/25/2014.
 */
public class H2MemoryServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2MemoryServerTest.class);

    @Test
    public void test_create_in_memory_h2() {
        try {
            @Cleanup H2MemoryServer h2MemoryServer = new H2MemoryServer();
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            assertFalse(connection.isClosed());
        } catch(SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }
}
