package h2;

import lombok.Cleanup;
import model.TopScorer;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit Test the H2TopScorer class
 * Created by MC on 4/26/2014.
 */
public class H2TopScorerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2TopScorerTest.class);
    private static final String WAYNE_ROONEY = "Wayne Rooney";
    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int GOALS = 66;
    private static final int POSITION = 1;

    @After
    public void tearDown() {
        try {
            @Cleanup Connection connection = H2Utils.createInMemoryH2Connection();
            H2TopScorer.deleteTopScorers(connection);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on tear down!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_top_scorer_meta_data() {
        try {
            @Cleanup Connection connection = H2Utils.createInMemoryH2Connection();
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
            @Cleanup Connection connection = H2Utils.createInMemoryH2Connection();
            H2TopScorer.createTopScorersTable(connection);
            assertEquals(1, H2TopScorer.addTopScorer(connection, POSITION, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));

            List<TopScorer> tops = H2TopScorer.getTopScorers(connection);
            assertEquals(1, tops.size());
            TopScorer topScorer = tops.get(0);

            assertEquals(POSITION, topScorer.getPosition());
            assertEquals(WAYNE_ROONEY, topScorer.getName());
            assertEquals(MANCHESTER_UNITED, topScorer.getTeam());
            assertEquals(GOALS, topScorer.getGoals());

        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_top_scorer_add_and_delete() {
        try {
            @Cleanup Connection connection = H2Utils.createInMemoryH2Connection();
            H2TopScorer.createTopScorersTable(connection);
            assertEquals(1, H2TopScorer.addTopScorer(connection, POSITION, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));
            assertEquals(1, H2TopScorer.deleteTopScorers(connection));
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    private void assertResultSetMetaData(ResultSet topScores) throws SQLException {
        ResultSetMetaData metaData = topScores.getMetaData();
        assertEquals(4, metaData.getColumnCount());
        assertEquals("POSITION", metaData.getColumnName(1));
        assertEquals("INTEGER", metaData.getColumnTypeName(1));
        assertEquals("NAME", metaData.getColumnName(2));
        assertEquals("VARCHAR", metaData.getColumnTypeName(2));
        assertEquals("TEAM", metaData.getColumnName(3));
        assertEquals("VARCHAR", metaData.getColumnTypeName(3));
        assertEquals("GOALS", metaData.getColumnName(4));
        assertEquals("INTEGER", metaData.getColumnTypeName(4));
    }
}
