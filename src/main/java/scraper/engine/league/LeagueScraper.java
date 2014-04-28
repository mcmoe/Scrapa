package scraper.engine.league;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder;
import h2.connection.H2EmbeddedServer;
import h2.table.H2ScrapaData;
import lombok.Cleanup;
import model.ScrapaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scraper.wrapper.LeagueScraperData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * Scrapes season team goals and top scorers information from free-elements
 * Created by mcmoe on 4/26/2014.
 */
public class LeagueScraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeagueScraper.class);
    static final String HTTP_DOMAIN = "http://www.free-elements.com/";

    private Set<String> relativePaths;
    private Iterator<String> iterator;
    private MechanizeAgent mechanizeAgent;
    private H2EmbeddedServer db;

    public LeagueScraper(Set<String> relativePaths) {
        this.relativePaths = relativePaths;
        this.iterator = relativePaths.iterator();
        mechanizeAgent = new MechanizeAgent();
        db = new H2EmbeddedServer();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public LeagueScraperData scrapeNext() {
        return scrapeWeb(iterator.next());
    }

    public Set<String> getRelativePaths() {
        return relativePaths;
    }

    private LeagueScraperData scrapeWeb(String relativePath) {
        String season = HTTP_DOMAIN + relativePath;
        Optional<ScrapaData> cachedData = getCachedDataIfDataFromThePast(season);

        String data;
        if(cachedData.isPresent()) {
            data = cachedData.get().getData();
        } else {
            data = scrapeNow(season);
        }

        return new LeagueScraperData(season, data);
    }

    /**
     * Only checks for cached data if the data is from the past (i.e not current season)
     * This both helps avoid uselessly reading the db or worse retrieve outdated data!
     * Algorithm: "Seasons" appears in the url of all past data - (for player tables ist "Goals")
     *
     * @param season the season url to scrape
     * @return the past cached data or nothing
     */
    private Optional<ScrapaData> getCachedDataIfDataFromThePast(String season) {
        Optional<ScrapaData> cachedData = Optional.empty();
        if(isFromThePast(season)) {
            return getCachedData(season);
        }
        return cachedData;
    }

    private String scrapeNow(String season) {
        HtmlDocument page = mechanizeAgent.get(season);
        HtmlElement table = page.htmlElements().get(HtmlQueryBuilder.byTag("tbody"));
        String normalizedXml = normalizeXml(table.toString());
        cacheDataIfDataFromThePast(season, normalizedXml);
        LOGGER.info(normalizedXml);
        return normalizedXml;
    }

    private void cacheDataIfDataFromThePast(String season, String data) {
        if(isFromThePast(season)) {
            cacheData(season, data);
        }
    }

    private boolean isFromThePast(String season) {
        return season.contains("Seasons");
    }

    private void cacheData(String url, String data) {
        try {
            H2ScrapaData.addScrapaData(db.getConnection(), url, data);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception while attempting to save Cache Data", e);
        }
    }

    private Optional<ScrapaData> getCachedData(String relativePath) {
        Optional<ScrapaData> scrapaData = Optional.empty();
        try {
            @Cleanup Connection connection = db.getConnection();
            H2ScrapaData.createScrapaDataTable(connection);
            scrapaData = H2ScrapaData.getScrapaDataWhere(connection, relativePath);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception while attempting to get Cached Data", e);
        }
        return scrapaData;
    }

    private static String normalizeXml(String xml) {
        String oneLineXml = xml.replaceAll("&nbsp;", "");
        List<String> words = new ArrayList<>();
        Collections.addAll(words, oneLineXml.split("\n"));
        return words.stream().map(String::trim).collect(joining(""));
    }

    public void close() {
        db.close();
    }
}
