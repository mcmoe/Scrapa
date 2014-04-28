package h2.sql;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test the TeamGoalsSQL class
 * Created by mcmoe on 4/28/2014.
 */
public class TeamGoalsSQLTest {

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS TEAM_GOALS(TEAM VARCHAR(255), GOALS INT, PRIMARY KEY (TEAM))";
    private static final String INSERT = "INSERT INTO TEAM_GOALS (TEAM, GOALS) VALUES (?,?)";
    private static final String SELECT = "SELECT * FROM TEAM_GOALS";
    private static final String DELETE = "DELETE FROM TEAM_GOALS";

    @Test
    public void test_create_table_sql() {
        assertEquals(CREATE, TeamGoalsSQL.CREATE_TEAM_GOALS_TABLE);
    }

    @Test
    public void test_add_to_table_sql() {
        assertEquals(INSERT, TeamGoalsSQL.ADD_TEAM_GOALS);
    }

    @Test
    public void test_get_from_table_sql() {
        assertEquals(SELECT, TeamGoalsSQL.GET_TEAM_GOALS);
    }

    @Test
    public void test_delete_from_table_sql() {
        assertEquals(DELETE, TeamGoalsSQL.DELETE_TEAM_GOALS);
    }
}
