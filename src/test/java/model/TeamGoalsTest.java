package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Unit Test the TeamGoals object
 * Created by MC on 4/26/2014.
 */
public class TeamGoalsTest {

    private static final int POSITION = 1;
    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int GOALS = 123;

    @Test
    public void test_create_team_goals() {
        TeamGoals teamGoals = new TeamGoals(POSITION, MANCHESTER_UNITED, GOALS);
        assertNotNull(teamGoals);
    }

    @Test
    public void test_team_goals_getters_consistency() {
        TeamGoals teamGoals = new TeamGoals(POSITION, MANCHESTER_UNITED, GOALS);

        assertEquals(POSITION, teamGoals.getPosition());
        assertEquals(MANCHESTER_UNITED, teamGoals.getTeam());
        assertEquals(GOALS, teamGoals.getGoals());
    }
}
