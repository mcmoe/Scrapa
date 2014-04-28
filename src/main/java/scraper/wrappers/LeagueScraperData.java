package scraper.wrappers;

import lombok.Getter;

/**
 * Represents scraped league data and its web source
 * Created by mcmoe on 4/28/2014.
 */
public class LeagueScraperData {

    @Getter private final String webSource;
    @Getter private final String xmlTable;

    public LeagueScraperData(String webSource, String xmlTable) {
        this.webSource = webSource;
        this.xmlTable = xmlTable;
    }
}
