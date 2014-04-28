package h2;

import h2.sql.TeamGoalsSQL;
import lombok.Cleanup;
import model.TeamGoals;
import org.junit.After;
import org.junit.Test;
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

    @After
    public void tearDown() {
        try {
            @Cleanup Connection connection = H2Server.createInMemoryH2Connection();
            H2TeamGoals.deleteTeamGoals(connection);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on tear down!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_team_goals_meta_data() {
        try {
            @Cleanup Connection connection = H2Server.createInMemoryH2Connection();
            H2TeamGoals.createTeamGoalsTable(connection);

            @Cleanup Statement statement = H2Server.createStatement(connection);
            @Cleanup ResultSet teamGoals = H2TeamGoals.getTeamGoals(statement);
            assertResultSetMetaData(teamGoals);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_team_goals_add_and_get() {
        try {
            @Cleanup Connection connection = H2Server.createInMemoryH2Connection();
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
        @Cleanup Connection connection = H2Server.createInMemoryH2Connection();
        H2TeamGoals.createTeamGoalsTable(connection);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS);
    }

    @Test(expected = SQLException.class)
    public void test_team_goals_add_primary_key_duplicate() throws SQLException {
        @Cleanup Connection connection = H2Server.createInMemoryH2Connection();
        H2TeamGoals.createTeamGoalsTable(connection);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS);
        H2TeamGoals.addTeamGoals(connection, MANCHESTER_UNITED, GOALS + 1);
    }

    @Test
    public void test_team_goals_add_and_delete() {
        try {
            @Cleanup Connection connection = H2Server.createInMemoryH2Connection();
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
