package h2.sql;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test the TopScorerSQL class
 * Created by mcmoe on 4/28/2014.
 */
public class TopScorerSQLTest {

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS TOP_SCORERS(PLAYER VARCHAR(255), TEAM VARCHAR(255), GOALS INT, PRIMARY KEY (PLAYER, TEAM))";
    private static final String INSERT = "INSERT INTO TOP_SCORERS (PLAYER, TEAM, GOALS) VALUES (?,?,?)";
    private static final String SELECT = "SELECT * FROM TOP_SCORERS";
    private static final String DELETE = "DELETE FROM TOP_SCORERS";

    @Test
    public void test_create_table_sql() {
        assertEquals(CREATE, TopScorerSQL.CREATE_TOP_SCORERS_TABLE);
    }

    @Test
    public void test_add_to_table_sql() {
        assertEquals(INSERT, TopScorerSQL.ADD_TOP_SCORER);
    }

    @Test
    public void test_get_from_table_sql() {
        assertEquals(SELECT, TopScorerSQL.GET_TOP_SCORERS);
    }

    @Test
    public void test_delete_from_table_sql() {
        assertEquals(DELETE, TopScorerSQL.DELETE_TOP_SCORERS);
    }
}
