package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test the league standing model
 * Created by mcmoe on 4/28/2014.
 */
public class LeagueStandingTest {

    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int STANDING = 1;

    @Test
    public void test_league_standing_creation() {
        LeagueStanding leagueStanding = new LeagueStanding(MANCHESTER_UNITED, STANDING);
        assertNotNull(leagueStanding);
    }

    @Test
    public void test_league_standing_getters_consistency() {
        LeagueStanding leagueStanding = new LeagueStanding(MANCHESTER_UNITED, STANDING);
        assertEquals(MANCHESTER_UNITED, leagueStanding.getTeam());
        assertEquals(STANDING, leagueStanding.getStanding());
    }
}
