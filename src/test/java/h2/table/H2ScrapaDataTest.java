package h2.table;

import commons.Utils;
import h2.connection.H2MemoryServer;
import h2.connection.H2Utils;
import h2.sql.ScrapaDataSQL;
import lombok.Cleanup;
import model.ScrapaData;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit Test the H2ScrapaDataTest class
 * Created by mcmoe on 4/28/2014.
 */


public class H2ScrapaDataTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2ScrapaDataTest.class);
    private static final String URL = "some/thing/like/this.html";
    private static final String DATA = getScrapedData();

    private static H2MemoryServer h2MemoryServer;

    private static String getScrapedData() {
        String scrapedData = "";
        try {
            scrapedData = Utils.getString(H2ScrapaDataTest.class.getResourceAsStream("scrapedData"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to get Mock Data!");
        }
        return scrapedData;
    }

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
            H2ScrapaData.deleteScrapaData(connection);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on reset table!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_scrapa_data_meta_data() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2ScrapaData.createScrapaDataTable(connection);

            @Cleanup Statement statement = H2Utils.createStatement(connection);
            @Cleanup ResultSet scrapaData = H2ScrapaData.getScrapaData(statement);
            assertResultSetMetaData(scrapaData);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_scrapa_data_add_and_get() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2ScrapaData.createScrapaDataTable(connection);
            assertEquals(1, H2ScrapaData.addScrapaData(connection, URL, DATA));

            List<ScrapaData> scrapaDataList = H2ScrapaData.getScrapaData(connection);
            assertEquals(1, scrapaDataList.size());
            ScrapaData scrapaData = scrapaDataList.get(0);

            assertEquals(URL, scrapaData.getUrl());
            assertEquals(DATA, scrapaData.getData());

        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_scrapa_data_add_and_get_where() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2ScrapaData.createScrapaDataTable(connection);
            assertEquals(1, H2ScrapaData.addScrapaData(connection, URL, DATA));

            Optional<ScrapaData> scrapaData = H2ScrapaData.getScrapaDataWhere(connection, URL);
            assertTrue(scrapaData.isPresent());

            assertEquals(URL, scrapaData.get().getUrl());
            assertEquals(DATA, scrapaData.get().getData());

        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test(expected = SQLException.class)
    public void test_scrapa_data_add_duplicate() throws SQLException {
        @Cleanup Connection connection = h2MemoryServer.getConnection();
        H2ScrapaData.createScrapaDataTable(connection);
        H2ScrapaData.addScrapaData(connection, URL, DATA);
        H2ScrapaData.addScrapaData(connection, URL, DATA);
    }

    @Test(expected = SQLException.class)
    public void test_scrapa_data_add_primary_key_duplicate() throws SQLException {
        @Cleanup Connection connection = h2MemoryServer.getConnection();
        H2ScrapaData.createScrapaDataTable(connection);
        H2ScrapaData.addScrapaData(connection, URL, DATA);
        H2ScrapaData.addScrapaData(connection, URL, DATA + 1);
    }

    @Test
    public void test_scrapa_data_add_and_delete() {
        try {
            @Cleanup Connection connection = h2MemoryServer.getConnection();
            H2ScrapaData.createScrapaDataTable(connection);
            assertEquals(1, H2ScrapaData.addScrapaData(connection, URL, DATA));
            assertEquals(1, H2ScrapaData.deleteScrapaData(connection));
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered !", e);
            fail("SQL Exception - check logs");
        }
    }

    private void assertResultSetMetaData(ResultSet scrapaData) throws SQLException {
        ResultSetMetaData metaData = scrapaData.getMetaData();
        assertEquals(2, metaData.getColumnCount());

        assertEquals("URL", metaData.getColumnName(ScrapaDataSQL.COLUMNS.URL.index()));
        assertEquals("VARCHAR", metaData.getColumnTypeName(ScrapaDataSQL.COLUMNS.URL.index()));

        assertEquals("DATA", metaData.getColumnName(ScrapaDataSQL.COLUMNS.DATA.index()));
        assertEquals("CLOB", metaData.getColumnTypeName(ScrapaDataSQL.COLUMNS.DATA.index()));
    }
}
