package h2;

import lombok.Cleanup;
import model.TopScorer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void test_top_scorer_meta_data() {
        try {
            @Cleanup Connection connection = H2Utils.createInMemoryH2Connection();
            H2TopScorer.createTopScorersTable(connection);
            assertEquals(1, H2TopScorer.addTopScorer(connection, POSITION, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));

            @Cleanup Statement statement = H2Utils.createStatement(connection);
            ResultSet topScores = H2TopScorer.getTopScorers(statement);
            assertResultSetMetaData(topScores);
            assertResultSetCount(topScores, 1);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
        }

    }

    @Test public void test_top_scorer_add_and_get() {
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
        }
    }

    @Test public void test_top_scorer_add_and_delete() {
        try {
            @Cleanup Connection connection = H2Utils.createInMemoryH2Connection();
            H2TopScorer.createTopScorersTable(connection);
            assertEquals(1, H2TopScorer.addTopScorer(connection, POSITION, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));

            int rowsDeleted = H2TopScorer.deleteTopScorers(connection);
            assertEquals(1, rowsDeleted);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
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

    private void assertResultSetCount(ResultSet topScores, int expected) throws SQLException {
        topScores.last();
        assertEquals(expected, topScores.getRow());
        topScores.beforeFirst();
    }
}
