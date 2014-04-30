package h2.table;

import commons.Utils;
import h2.connection.H2MemoryServer;
import h2.sql.ScrapaDataSQL;
import h2.table.commons.ColumnsMeta;
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
    private static final Timestamp CURRENT_TIMESTAMP_UTC = Utils.getCurrentTimeStampUTC();

    private static H2MemoryServer h2MemoryServer;
    private static H2ScrapaData h2ScrapaData;

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
        try {
            h2ScrapaData = new H2ScrapaData(h2MemoryServer.getConnection());
            h2ScrapaData.createScrapaDataTable();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception on setUp!", e);
            fail("setUp Exception - check logs");
        }
    }

    @AfterClass
    public static void tearDown() {
        h2ScrapaData.close();
        h2MemoryServer.close();
    }

    @After
    public void resetTable() {
        try {
            h2ScrapaData.deleteScrapaData();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered on reset table!", e);
            fail("SQL Exception on tear down - check logs");
        }
    }

    @Test
    public void test_table_columns_meta_data() {
        try {
            @Cleanup ResultSet metaData = h2ScrapaData.getMetaData();
            assertColumnsMetaData(metaData);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception encountered!", e);
            fail("SQL Exception - check logs");
        }
    }

    @Test
    public void test_scrapa_data_add_and_get() throws SQLException {
        assertEquals(1, h2ScrapaData.addScrapaData(URL, DATA));

        List<ScrapaData> scrapaDataList = h2ScrapaData.getScrapaData();
        assertEquals(1, scrapaDataList.size());
        ScrapaData scrapaData = scrapaDataList.get(0);

        assertEquals(URL, scrapaData.getUrl());
        assertEquals(DATA, scrapaData.getData());
        assertTrue(CURRENT_TIMESTAMP_UTC.before(scrapaData.getAddedOnUTC()));
    }

    @Test
    public void test_scrapa_data_add_and_get_where() throws SQLException {
        assertEquals(1, h2ScrapaData.addScrapaData(URL, DATA));

        Optional<ScrapaData> scrapaData = h2ScrapaData.getScrapaDataWhere(URL);
        assertTrue(scrapaData.isPresent());

        assertEquals(URL, scrapaData.get().getUrl());
        assertEquals(DATA, scrapaData.get().getData());
        assertTrue(CURRENT_TIMESTAMP_UTC.before(scrapaData.get().getAddedOnUTC()));
    }

    @Test(expected = SQLException.class)
    public void test_scrapa_data_add_duplicate() throws SQLException {
        h2ScrapaData.addScrapaData(URL, DATA);
        h2ScrapaData.addScrapaData(URL, DATA);
    }

    @Test(expected = SQLException.class)
    public void test_scrapa_data_add_primary_key_duplicate() throws SQLException {
        h2ScrapaData.addScrapaData(URL, DATA);
        h2ScrapaData.addScrapaData(URL, DATA + 1);
    }

    @Test
    public void test_scrapa_data_add_and_delete() throws SQLException {
        assertEquals(1, h2ScrapaData.addScrapaData(URL, DATA));
        assertEquals(1, h2ScrapaData.deleteScrapaData());
    }

    private void assertColumnsMetaData(ResultSet metaData) throws SQLException {
        metaData.next();
        assertEquals(metaData.getRow(), ScrapaDataSQL.COLUMNS.URL.index());
        assertEquals("URL", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("VARCHAR", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        metaData.next();
        assertEquals(metaData.getRow(), ScrapaDataSQL.COLUMNS.DATA.index());
        assertEquals("DATA", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("CLOB", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        metaData.next();
        assertEquals(metaData.getRow(), ScrapaDataSQL.COLUMNS.ADDED_ON_UTC.index());
        assertEquals("ADDED_ON_UTC", metaData.getString(ColumnsMeta.DATA.COLUMN_NAME.index()));
        assertEquals("TIMESTAMP", metaData.getString(ColumnsMeta.DATA.TYPE_NAME.index()));

        assertEquals("there should not be anymore rows!", false, metaData.next());
    }
}
