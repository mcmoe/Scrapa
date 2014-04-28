package h2.table;

import h2.connection.H2MemoryServer;
import h2.connection.H2Utils;
import h2.sql.TeamGoalsSQL;
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
            H2TeamGoals.deleteTeamGoals(connection);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on reset table!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_team_goals_meta_data() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2TeamGoals.createTeamGoalsTable(connection);

            @Cleanup Statement statement = H2Utils.createStatement(connection);
            @Cleanup ResultSet teamGoals = H2TeamGoals.getTeamGoals(statement);
            assertResultSetMetaData(teamGoals);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_team_goals_add_and_get() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2TeamGoals.createTeamGoalsTable(connection);
            assertEquals(1, H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS));

            List<TeamGoals> teams = H2TeamGoals.getTeamGoals(connection);
            assertEquals(1, teams.size());
            TeamGoals teamGoals = teams.get(0);

            assertEquals(MANCHESTER_UNITED, teamGoals.getTeam());
            assertEquals(GOALS, teamGoals.getGoals());

        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test(expected = SQLException.class)
    public void test_team_goals_add_duplicate() throws SQLException {
        @Cleanup Connection connection = h2MemoryServer.getConnection();
        H2TeamGoals.createTeamGoalsTable(connection);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS);
    }

    @Test(expected = SQLException.class)
    public void test_team_goals_add_primary_key_duplicate() throws SQLException {
        @Cleanup Connection connection = h2MemoryServer.getConnection();
        H2TeamGoals.createTeamGoalsTable(connection);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS + 1);
    }

    @Test
    public void test_team_goals_add_and_delete() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2TeamGoals.createTeamGoalsTable(connection);
            assertEquals(1, H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS));
            assertEquals(1, H2TeamGoals.deleteTeamGoals(connection));
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    private void assertResultSetMetaData(ResultSet teamGoals) throws SQLException {
        ResultSetMetaData metaData = teamGoals.getMetaData();
        assertEquals(2, metaData.getColumnCount());

        assertEquals("TEAM", metaData.getColumnName(TeamGoalsSQL.COLUMNS.TEAM.index()));
        assertEquals("VARCHAR", metaData.getColumnTypeName(TeamGoalsSQL.COLUMNS.TEAM.index()));

        assertEquals("GOALS", metaData.getColumnName(TeamGoalsSQL.COLUMNS.GOALS.index()));
        assertEquals("INTEGER", metaData.getColumnTypeName(TeamGoalsSQL.COLUMNS.GOALS.index()));
    }
}
