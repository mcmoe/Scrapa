package h2;

import lombok.Cleanup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit test the H2 DB API
 * Created by MC on 4/25/2014.
 */
public class H2Test {

    private static final Logger logger = LoggerFactory.getLogger(H2Test.class);
    private static final String WAYNE_ROONEY = "Wayne Rooney";
    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int GOALS = 66;
    private static final int POSITION = 1;

    @Test
    public void test_create_h2_add_and_query() {
        try {
            @Cleanup Connection connection = H2Utils.createInMemoryH2Connection();
            //@Cleanup Connection connection = H2Utils.createEmbeddedH2Connection();
            @Cleanup Statement statement = H2Utils.createStatement(connection);
            H2TopScorer.createTopScorersTable(statement);

            @Cleanup PreparedStatement addTopScorerStatement = H2TopScorer.prepareAddTopScorersStatement(connection);
            assertEquals(1, H2TopScorer.addTopScorer(addTopScorerStatement, POSITION, WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));

            ResultSet topScores = H2TopScorer.getTopScorers(statement);
            assertColumnMetaData(topScores);
            assertResultHasOnePlayer(topScores);
            assertTopScorer(topScores);
        } catch(SQLException e) {
            logger.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }

    private void assertResultHasOnePlayer(ResultSet topScores) throws SQLException {
        topScores.last();
        assertEquals(1, topScores.getRow());
        topScores.beforeFirst();
    }

    private void assertTopScorer(ResultSet topScores) throws SQLException {
        topScores.next();
        assertEquals(POSITION, topScores.getInt(1));
        assertEquals(WAYNE_ROONEY, topScores.getString(2));
        assertEquals(MANCHESTER_UNITED, topScores.getString(3));
        assertEquals(GOALS, topScores.getInt(4));
    }

    private void assertColumnMetaData(ResultSet topScores) throws SQLException {
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
