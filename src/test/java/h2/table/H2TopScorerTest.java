package h2.table;

import h2.connection.H2MemoryServer;
import h2.connection.H2Utils;
import h2.sql.TopScorersSQL;
import lombok.Cleanup;
import model.TopScorer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit Test the H2TopScorer class
 * Created by mcmoe on 4/26/2014.
 */
public class H2TopScorerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2TopScorerTest.class);
    private static final String WAYNE_ROONEY = "Wayne Rooney";
    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int GOALS = 66;

    private static H2MemoryServer h2MemoryServer;

    @BeforeClass
    public static void setUp() {
        h2MemoryServer = new H2MemoryServer();
    }

    @AfterClass
    public static void tearDown() {
        h2MemoryServer.close();
    }

    @After
    public void resetTable() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2TopScorer.deleteTopScorers(connection);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on tear down!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_top_scorer_meta_data() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2TopScorer.createTopScorersTable(connection);

            @Cleanup Statement statement = H2Utils.createStatement(connection);
            @Cleanup ResultSet topScores = H2TopScorer.getTopScorers(statement);
            assertResultSetMetaData(topScores);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_top_scorer_add_and_get() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2TopScorer.createTopScorersTable(connection);
            assertEquals(1, H2TopScorer.addTopScorer(connection, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));

            List<TopScorer> tops = H2TopScorer.getTopScorers(connection);
            assertEquals(1, tops.size());
            TopScorer topScorer = tops.get(0);

            assertEquals(WAYNE_ROONEY, topScorer.getPlayer());
            assertEquals(MANCHESTER_UNITED, topScorer.getTeam());
            assertEquals(GOALS, topScorer.getGoals());

        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test(expected = SQLException.class)
    public void test_top_scorer_add_duplicate() throws SQLException {
        @Cleanup H2MemoryServer h2MemoryServer = new H2MemoryServer();
        @Cleanup Connection connection = h2MemoryServer.getConnection();
        H2TopScorer.createTopScorersTable(connection);
        H2TopScorer.addTopScorer(connection, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);
        H2TopScorer.addTopScorer(connection, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);
    }

    @Test(expected = SQLException.class)
    public void test_top_scorer_add_primary_key_duplicate() throws SQLException {
        @Cleanup H2MemoryServer h2MemoryServer = new H2MemoryServer();
        @Cleanup Connection connection = h2MemoryServer.getConnection();
        H2TopScorer.createTopScorersTable(connection);
        H2TopScorer.addTopScorer(connection, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);
        H2TopScorer.addTopScorer(connection, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS+1);
    }

    @Test
    public void test_top_scorer_add_and_delete() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2TopScorer.createTopScorersTable(connection);
            assertEquals(1, H2TopScorer.addTopScorer(connection, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));
            assertEquals(1, H2TopScorer.deleteTopScorers(connection));
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    private void assertResultSetMetaData(ResultSet topScores) throws SQLException {
        ResultSetMetaData metaData = topScores.getMetaData();
        assertEquals(3, metaData.getColumnCount());

        assertEquals("PLAYER", metaData.getColumnName(TopScorersSQL.COLUMNS.PLAYER.index()));
        assertEquals("VARCHAR", metaData.getColumnTypeName(TopScorersSQL.COLUMNS.PLAYER.index()));

        assertEquals("TEAM", metaData.getColumnName(TopScorersSQL.COLUMNS.TEAM.index()));
        assertEquals("VARCHAR", metaData.getColumnTypeName(TopScorersSQL.COLUMNS.TEAM.index()));

        assertEquals("GOALS", metaData.getColumnName(TopScorersSQL.COLUMNS.GOALS.index()));
        assertEquals("INTEGER", metaData.getColumnTypeName(TopScorersSQL.COLUMNS.GOALS.index()));
    }
}
