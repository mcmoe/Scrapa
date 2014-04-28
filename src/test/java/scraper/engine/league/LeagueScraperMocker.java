package scraper.engine.league;

import lombok.Cleanup;
import scraper.wrappers.LeagueScraperData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

/**
 * Mock Data Objects returned by LeagueScraper
 * Created by mcmoe on 4/28/2014.
 */
public class LeagueScraperMocker {
    public static LeagueScraperData getMockedData(String season, String scrapedTable) throws IOException {
        String tableXml = scrapeMock(scrapedTable);
        return new LeagueScraperData(season, tableXml);
    }

    static String scrapeMock(String scrapedTable) throws IOException {
        @Cleanup InputStream inStream = LeagueScraperMocker.class.getResourceAsStream(scrapedTable);
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        return reader.lines().collect(joining(""));
    }
}
