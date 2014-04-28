package scraper.engine.league;

import com.google.common.collect.Sets;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scraper.wrappers.LeagueScraperData;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.*;
import static scraper.engine.league.LeagueScraperMocker.scrapeMock;

/**
 * Attempt to load web page and scrape for data
 * Created by mcmoe on 4/19/2014.
 */
public class LeagueScraperTest {
    /* PLEASE NOTE : scraping tests are ignored to avoid web spamming
    *  please launch manually before any commit! */
    private static final Logger LOGGER = LoggerFactory.getLogger(LeagueScraperTest.class);

    @Test @Ignore
    public void test_get_2012_top_scorers_and_scrape() {
        try {
            String season = "England/Seasons/S2012.html";
            LeagueScraper leagueScraper = new LeagueScraper(Sets.newHashSet(season));
            LeagueScraperData leagueScraperData = leagueScraper.scrapeNext();
            assertEquals(scrapeMock("scraped2012Table"), leagueScraperData.getXmlTable());
            assertEquals(LeagueScraper.HTTP_DOMAIN + season, leagueScraperData.getWebSource());
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    @Test @Ignore
    public void test_get_2013_top_scorers_and_scrape() {
        try {
            String season = "England/S2013.html";
            LeagueScraper leagueScraper = new LeagueScraper(Sets.newHashSet(season));
            LeagueScraperData leagueScraperData = leagueScraper.scrapeNext();
            assertEquals(scrapeMock("scraped2013Table"), leagueScraperData.getXmlTable());
            assertEquals(LeagueScraper.HTTP_DOMAIN + season, leagueScraperData.getWebSource());
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    @Test @Ignore
    public void test_get_1986_top_scorers_and_scrape() {
        try {
            String season = "England/Seasons/S1986.html";
            LeagueScraper leagueScraper = new LeagueScraper(Sets.newHashSet(season));
            LeagueScraperData leagueScraperData = leagueScraper.scrapeNext();
            assertEquals(scrapeMock("scraped1986Table"), leagueScraperData.getXmlTable());
            assertEquals(LeagueScraper.HTTP_DOMAIN + season, leagueScraperData.getWebSource());
        } catch(IOException e) {
            LOGGER.error("exception in unit test", e);
            fail("an exception was thrown!");
        }
    }

    @Test @Ignore
    public void test_has_next_on_two_paths() {
        LeagueScraper leagueScraper = new LeagueScraper(Sets.newHashSet("a", "b"));
        assertTrue(leagueScraper.hasNext());

        try { leagueScraper.scrapeNext(); }
        catch(NullPointerException e) { assertTrue(leagueScraper.hasNext()); }

        try { leagueScraper.scrapeNext(); }
        catch(NullPointerException e) { assertFalse(leagueScraper.hasNext()); }
    }

    @Test
    public void test_get_relative_paths() {
        LeagueScraper leagueScraper = new LeagueScraper(Sets.newHashSet("a", "b"));
        Iterator<String> iterator = leagueScraper.getRelativePaths().iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
    }
}
