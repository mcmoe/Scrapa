package model;

import commons.Utils;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Unit Test the ScrapaData object
 * Created by mcmoe on 4/28/2014.
 */
public class ScrapaDataTest {

    private static final String URL = "some/thing/like/this.html";
    private static final String DATA = getScrapedData();
    private static final Timestamp CURRENT_TIMESTAMP_UTC = Utils.getCurrentTimeStampUTC();

    private static String getScrapedData() {
        String scrapedData = "";
        try {
            scrapedData = Utils.getString(ScrapaDataTest.class.getResourceAsStream("scrapedData"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to get Mock Data!");
        }
        return scrapedData;
    }

    @Test
    public void test_create_scrapa_data() {
        ScrapaData scrapaData = new ScrapaData(URL, DATA, CURRENT_TIMESTAMP_UTC);
        assertNotNull(scrapaData);
    }

    @Test
    public void test_scrapa_data_getters_consistency() {
        ScrapaData scrapaData = new ScrapaData(URL, DATA, CURRENT_TIMESTAMP_UTC);

        assertEquals(URL, scrapaData.getUrl());
        assertEquals(DATA, scrapaData.getData());
        assertEquals(CURRENT_TIMESTAMP_UTC, scrapaData.getAddedOnUTC());
    }
}
