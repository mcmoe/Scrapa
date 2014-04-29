package h2.table;

import h2.connection.H2MemoryServer;
import h2.sql.TopScorerSQL;
import h2.table.commons.ColumnsMeta;
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
    private static H2TopScorer h2TopScorer;

    @BeforeClass
    public static void setUp() {
        h2MemoryServer = new H2MemoryServer();
        try {
            h2TopScorer = new H2TopScorer(h2MemoryServer.getConnection());
            h2TopScorer.createTopScorersTable();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception on setUp!", e);
            fail("setUp Exception - check logs");
        }
    }

    @AfterClass
    public static void tearDown() {
        h2TopScorer.close();
        h2MemoryServer.close();
    }

    @After
    public void resetTable() {
        try {
            h2TopScorer.deleteTopScorers();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on tear down!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_top_scorer_meta_data() throws SQLException {
        @Cleanup ResultSet metaData = h2TopScorer.getMetaData();
        assertColumnsMetaData(metaData);
    }

    @Test
    public void test_top_scorer_add_and_get() throws SQLException {
        h2TopScorer.createTopScorersTable();
        assertEquals(1, h2TopScorer.addTopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));

        List<TopScorer> tops = h2TopScorer.getTopScorers();
        assertEquals(1, tops.size());
        TopScorer topScorer = tops.get(0);

        assertEquals(WAYNE_ROONEY, topScorer.getPlayer());
        assertEquals(MANCHESTER_UNITED, topScorer.getTeam());
        assertEquals(GOALS, topScorer.getGoals());
    }

    @Test(expected = SQLException.class)
    public void test_top_scorer_add_duplicate() throws SQLException {
        h2TopScorer.createTopScorersTable();
        h2TopScorer.addTopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);
        h2TopScorer.addTopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);
    }

    @Test(expected = SQLException.class)
    public void test_top_scorer_add_primary_key_duplicate() throws SQLException {
        h2TopScorer.createTopScorersTable();
        h2TopScorer.addTopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);
        h2TopScorer.addTopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS+1);
    }

    @Test
    public void test_top_scorer_add_and_delete() throws SQLException {
        h2TopScorer.createTopScorersTable();
        assertEquals(1, h2TopScorer.addTopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS));
        assertEquals(1, h2TopScorer.deleteTopScorers());
    }

    private void assertColumnsMetaData(ResultSet metaData) throws SQLException {
        metaData.next();
        assertEquals(metaData.getRow(), TopScorerSQL.COLUMNS.PLAYER.index());
        assertEquals("PLAYER", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("VARCHAR", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        metaData.next();
        assertEquals(metaData.getRow(), TopScorerSQL.COLUMNS.TEAM.index());
        assertEquals("TEAM", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("VARCHAR", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        metaData.next();
        assertEquals(metaData.getRow(), TopScorerSQL.COLUMNS.GOALS.index());
        assertEquals("GOALS", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("INTEGER", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        assertEquals("there should not be anymore rows!", false, metaData.next());
    }
}
