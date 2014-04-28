package scraper.wrapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit Test the LeagueScraperData class
 * Created by mcmoe on 4/28/2014.
 */
public class LeagueScraperDataTest {

    private static final String WEB_SOURCE = "webSource";
    private static final String XML_TABLE = "xmlTable";

    @Test
    public void test_league_scraper_data_construction() {
        LeagueScraperData data = new LeagueScraperData(WEB_SOURCE, XML_TABLE);
        assertNotNull(data);
    }

    @Test
    public void test_league_scraper_data_getters_consistency() {
        LeagueScraperData data = new LeagueScraperData(WEB_SOURCE, XML_TABLE);
        assertEquals(WEB_SOURCE, data.getWebSource());
        assertEquals(XML_TABLE, data.getXmlTable());
    }
}
