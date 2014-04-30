package h2.sql;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test the ScrapaDataSQL class
 * Created by mcmoe on 4/28/2014.
 */
public class ScrapaDataSQLTest {
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS SCRAPA_DATA(URL VARCHAR(255), DATA CLOB, ADDED_ON_UTC TIMESTAMP, PRIMARY KEY (URL))";
    private static final String INSERT = "INSERT INTO SCRAPA_DATA (URL, DATA, ADDED_ON_UTC) VALUES (?,?,?)";
    private static final String SELECT = "SELECT * FROM SCRAPA_DATA";
    private static final String DELETE = "DELETE FROM SCRAPA_DATA";

    @Test
    public void test_create_table_sql() {
        assertEquals(CREATE, ScrapaDataSQL.CREATE_SCRAPA_DATA_TABLE);
    }

    @Test
    public void test_add_to_table_sql() {
        assertEquals(INSERT, ScrapaDataSQL.ADD_SCRAPA_URL_DATA);
    }

    @Test
    public void test_get_from_table_sql() {
        assertEquals(SELECT, ScrapaDataSQL.GET_SCRAPA_DATA);
    }

    @Test
    public void test_delete_from_table_sql() {
        assertEquals(DELETE, ScrapaDataSQL.DELETE_SCRAPA_DATA);
    }
}
