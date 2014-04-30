package h2.table;

import h2.connection.H2MemoryServer;
import h2.sql.LeagueStandingSQL;
import h2.table.commons.ColumnsMeta;
import lombok.Cleanup;
import model.LeagueStanding;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit Test the H2LeagueStandingTest class
 * Created by mcmoe on 4/30/2014.
 */
public class H2LeagueStandingTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2LeagueStandingTest.class);
    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int RANKING = 1;

    private static H2MemoryServer h2MemoryServer;
    private static H2LeagueStanding h2LeagueStanding;

    @BeforeClass
    public static void setUp() {
        h2MemoryServer = new H2MemoryServer();
        try {
            h2LeagueStanding = new H2LeagueStanding(h2MemoryServer.getConnection());
            h2LeagueStanding.createLeagueStandingTable();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception on setUp!", e);
            fail("setUp Exception - check logs");
        }
    }

    @AfterClass
    public static void tearDown() {
        h2LeagueStanding.close();
        h2MemoryServer.close();
    }

    @After
    public void resetTable() {
        try {
            h2LeagueStanding.deleteLeagueStandings();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on reset table!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_team_goals_meta_data() throws SQLException {
        @Cleanup ResultSet metaData = h2LeagueStanding.getMetaData();
        assertColumnsMetaData(metaData);
    }

    @Test
    public void test_team_goals_add_and_get() throws SQLException {
        assertEquals(1, h2LeagueStanding.addLeagueStanding(RANKING, MANCHESTER_UNITED));

        List<LeagueStanding> leagueStandings = h2LeagueStanding.getLeagueStandings();
        assertEquals(1, leagueStandings.size());
        LeagueStanding leagueStanding = leagueStandings.get(0);

        assertEquals(RANKING, leagueStanding.getStanding());
        assertEquals(MANCHESTER_UNITED, leagueStanding.getTeam());
    }

    @Test(expected = SQLException.class)
    public void test_team_goals_add_duplicate() throws SQLException {
        h2LeagueStanding.addLeagueStanding(RANKING, MANCHESTER_UNITED);
        h2LeagueStanding.addLeagueStanding(RANKING, MANCHESTER_UNITED);
    }

    @Test(expected = SQLException.class)
    public void test_team_goals_add_primary_key_duplicate() throws SQLException {
        h2LeagueStanding.addLeagueStanding(RANKING, MANCHESTER_UNITED);
        h2LeagueStanding.addLeagueStanding(RANKING, MANCHESTER_UNITED);
    }

    @Test
    public void test_team_goals_add_and_delete() throws SQLException {
        assertEquals(1, h2LeagueStanding.addLeagueStanding(RANKING, MANCHESTER_UNITED));
        assertEquals(1, h2LeagueStanding.deleteLeagueStandings());
    }

    private void assertColumnsMetaData(ResultSet metaData) throws SQLException {
        metaData.next();
        assertEquals(metaData.getRow(), LeagueStandingSQL.COLUMNS.RANKING.index());
        assertEquals("RANKING", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("INTEGER", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        metaData.next();
        assertEquals(metaData.getRow(), LeagueStandingSQL.COLUMNS.TEAM.index());
        assertEquals("TEAM", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("VARCHAR", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        assertEquals("there should not be anymore rows!", false, metaData.next());
    }
}
