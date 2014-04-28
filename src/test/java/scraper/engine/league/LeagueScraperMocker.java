package scraper.engine.league;

import commons.Utils;
import lombok.Cleanup;
import scraper.wrapper.LeagueScraperData;

import java.io.IOException;
import java.io.InputStream;

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
        return Utils.getString(inStream);
    }
}
