package scraper;

import lombok.Cleanup;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static scraper.Scraper.normalizeXml;

/**
 * Attempt to load web page and scrape for data
 * Created by mcmoe on 4/19/2014.
 */
public class ScraperTest {
    /* PLEASE NOTE : scraping tests are ignored to avoid web spamming
    *  please launch manually before any commit! */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperTest.class);

    @Test @Ignore
    public void test_get_2012_top_scorers_and_scrape() {
        try {
            String tableXml = Scraper.scrapeWeb("England/Seasons/S2012.html");
            assertEquals(scrapeMock("scraped2012Table"), tableXml);
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    @Test @Ignore
    public void test_get_2013_top_scorers_and_scrape() {
        try {
            String tableXml = Scraper.scrapeWeb("England/S2013.html");
            assertEquals(scrapeMock("scraped2013Table"), tableXml);
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    private String scrapeMock(String scrapedTable) throws IOException {
        @Cleanup InputStream inStream = getClass().getResourceAsStream(scrapedTable);
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        return normalizeXml(reader.lines().collect(joining("")));
    }
}
