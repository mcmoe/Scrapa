package h2.sql;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test the LeagueStandingSQL class
 * Created by mcmoe on 4/30/2014.
 */
public class LeagueStandingSQLTest {

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS LEAGUE_STANDING(RANKING INT, TEAM VARCHAR(255), PRIMARY KEY (RANKING, TEAM))";
    private static final String INSERT = "INSERT INTO LEAGUE_STANDING (RANKING, TEAM) VALUES (?,?)";
    private static final String SELECT = "SELECT * FROM LEAGUE_STANDING";
    private static final String DELETE = "DELETE FROM LEAGUE_STANDING";

    @Test
    public void test_create_table_sql() {
        assertEquals(CREATE, LeagueStandingSQL.CREATE_LEAGUE_STANDING_TABLE);
    }

    @Test
    public void test_add_to_table_sql() {
        assertEquals(INSERT, LeagueStandingSQL.ADD_LEAGUE_STANDING);
    }

    @Test
    public void test_get_from_table_sql() {
        assertEquals(SELECT, LeagueStandingSQL.GET_LEAGUE_STANDINGS);
    }

    @Test
    public void test_delete_from_table_sql() {
        assertEquals(DELETE, LeagueStandingSQL.DELETE_LEAGUE_STANDINGS);
    }
}
