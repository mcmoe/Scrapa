package scraper.engine;

import com.google.common.collect.Sets;
import lombok.Cleanup;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;

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
            Scraper scraper = new Scraper(Sets.newHashSet("England/Seasons/S2012.html"));
            assertEquals(scrapeMock("scraped2012Table"), scraper.scrapeNext());
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    @Test @Ignore
    public void test_get_2013_top_scorers_and_scrape() {
        try {
            Scraper scraper = new Scraper(Sets.newHashSet("England/S2013.html"));
            assertEquals(scrapeMock("scraped2013Table"), scraper.scrapeNext());
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    @Test @Ignore
    public void test_get_1986_top_scorers_and_scrape() {
        try {
            Scraper scraper = new Scraper(Sets.newHashSet("England/Seasons/S1986.html"));
            assertEquals(scrapeMock("scraped1986Table"), scraper.scrapeNext());
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    @Test
    public void test_has_next_on_two_paths() {
        Scraper scraper = new Scraper(Sets.newHashSet("a", "b"));
        assertTrue(scraper.hasNext());

        try { scraper.scrapeNext(); }
        catch(NullPointerException e) { assertTrue(scraper.hasNext()); }

        try { scraper.scrapeNext(); }
        catch(NullPointerException e) { assertFalse(scraper.hasNext()); }
    }

    @Test
    public void test_get_relative_paths() {
        Scraper scraper = new Scraper(Sets.newHashSet("a", "b"));
        Iterator<String> iterator = scraper.getRelativePaths().iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
    }

    private String scrapeMock(String scrapedTable) throws IOException {
        @Cleanup InputStream inStream = getClass().getResourceAsStream(scrapedTable);
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        return reader.lines().collect(joining(""));
    }
}
