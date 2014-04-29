package h2.table;

import h2.connection.H2MemoryServer;
import h2.sql.TeamGoalsSQL;
import h2.table.commons.ColumnsMeta;
import lombok.Cleanup;
import model.TeamGoals;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit Test the H2TeamGoals class
 * Created by mcmoe on 4/28/2014.
 */
public class H2TeamGoalsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2TeamGoalsTest.class);
    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int GOALS = 66;

    private static H2MemoryServer h2MemoryServer;
    private static H2TeamGoals h2TeamGoals;

    @BeforeClass
    public static void setUp() {
        h2MemoryServer = new H2MemoryServer();
        try {
            h2TeamGoals = new H2TeamGoals(h2MemoryServer.getConnection());
            h2TeamGoals.createTeamGoalsTable();
            } catch (SQLException e) {
            LOGGER.error("SQL Exception on setUp!", e);
            fail("setUp Exception - check logs");
        }
    }

    @AfterClass
    public static void tearDown() {
        h2TeamGoals.close();
        h2MemoryServer.close();
    }

    @After
    public void resetTable() {
        try {
            h2TeamGoals.deleteTeamGoals();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on reset table!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_team_goals_meta_data() throws SQLException {
        @Cleanup ResultSet teamGoals = h2TeamGoals.getMetaData();
        assertResultSetMetaData(teamGoals);
    }

    @Test
    public void test_team_goals_add_and_get() throws SQLException {
        assertEquals(1, h2TeamGoals.addTeamGoals(MANCHESTER_UNITED, GOALS));

        List<TeamGoals> teams = h2TeamGoals.getTeamGoals();
        assertEquals(1, teams.size());
        TeamGoals teamGoals = teams.get(0);

        assertEquals(MANCHESTER_UNITED, teamGoals.getTeam());
        assertEquals(GOALS, teamGoals.getGoals());
    }

    @Test(expected = SQLException.class)
    public void test_team_goals_add_duplicate() throws SQLException {
        h2TeamGoals.addTeamGoals(MANCHESTER_UNITED, GOALS);
        h2TeamGoals.addTeamGoals(MANCHESTER_UNITED, GOALS);
    }

    @Test(expected = SQLException.class)
    public void test_team_goals_add_primary_key_duplicate() throws SQLException {
        h2TeamGoals.addTeamGoals(MANCHESTER_UNITED, GOALS);
        h2TeamGoals.addTeamGoals(MANCHESTER_UNITED, GOALS + 1);
    }

    @Test
    public void test_team_goals_add_and_delete() throws SQLException {
        assertEquals(1, h2TeamGoals.addTeamGoals(MANCHESTER_UNITED, GOALS));
        assertEquals(1, h2TeamGoals.deleteTeamGoals());
    }

    private void assertResultSetMetaData(ResultSet metaData) throws SQLException {
        metaData.next();
        assertEquals(metaData.getRow(), TeamGoalsSQL.COLUMNS.TEAM.index());
        assertEquals("TEAM", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("VARCHAR", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        metaData.next();
        assertEquals(metaData.getRow(), TeamGoalsSQL.COLUMNS.GOALS.index());
        assertEquals("GOALS", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("INTEGER", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        assertEquals("there should not be anymore rows!", false, metaData.next());
    }
}
