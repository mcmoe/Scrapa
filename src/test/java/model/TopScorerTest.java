package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Unit Test the TopScorer object
 * Created by MC on 4/26/2014.
 */
public class TopScorerTest {

    private static final String WAYNE_ROONEY = "Wayne Rooney";
    private static final String MANCHESTER_UNITED = "Manchester United";
    private static final int GOALS = 66;

    @Test
    public void test_create_top_scorer() {
        TopScorer topScorer = new TopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);
        assertNotNull(topScorer);
    }
    @Test
    public void test_top_scorer_getters_consistency() {
        TopScorer topScorer = new TopScorer(WAYNE_ROONEY, MANCHESTER_UNITED, GOALS);

        assertEquals(WAYNE_ROONEY, topScorer.getPlayer());
        assertEquals(MANCHESTER_UNITED, topScorer.getTeam());
        assertEquals(GOALS, topScorer.getGoals());
    }
}
