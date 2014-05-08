package scraper.engine.league;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder;
import commons.Utils;
import h2.connection.H2EmbeddedServer;
import h2.table.H2ScrapaData;
import model.ScrapaData;
import org.joda.time.DateTime;
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
    private H2ScrapaData h2ScrapaData;
    private Connection connection;

    public LeagueScraper(Set<String> relativePaths) {
        this.relativePaths = relativePaths;
        this.iterator = relativePaths.iterator();
        mechanizeAgent = new MechanizeAgent();
        db = new H2EmbeddedServer();
        h2ScrapaData = null;
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
        Optional<ScrapaData> cachedData = getCachedDataIfValid(season);

        String data;
        if(cachedData.isPresent()) {
            data = cachedData.get().getData();
        } else {
            data = scrapeNow(season);
        }

        return new LeagueScraperData(season, data);
    }

    private String scrapeNow(String season) {
        HtmlDocument page = mechanizeAgent.get(season);
        HtmlElement table = page.htmlElements().get(HtmlQueryBuilder.byTag("tbody"));
        String normalizedXml = normalizeXml(table.toString());
        cacheData(season, normalizedXml);
        LOGGER.info(normalizedXml);
        return normalizedXml;
    }

    /**
     * if the data is from the past then we insert it since we know we will only be doing it once
     * if it is a current season, then we know we update it on a daily basis so a merge is required
     * @param season the url of the scraped data
     * @param data the scraped data
     */
    private void cacheData(String season, String data) {
        if(isFromThePast(season)) {
            addCacheData(season, data);
        } else {
            mergeCacheData(season, data);
        }
    }

    private boolean isFromThePast(String season) {
        return season.contains("Seasons");
    }

    private void mergeCacheData(String url, String data) {
        try {
            getH2ScrapaData().mergeScrapaData(url, data);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception while attempting to merge Cache Data", e);
        }
    }

    private void addCacheData(String url, String data) {
        try {
            getH2ScrapaData().addScrapaData(url, data);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception while attempting to insert Cache Data", e);
        }
    }

    /**
     * Only checks for cached data if the data is from the past (i.e not current season)
     * or if it is current season BUT less than a day old (since current season always updates)
     * This means that data from current season is accurate to one day from cached data at all times
     * Algorithm: "Seasons" appears in the url of all past data - (for player tables its "Goals")
     * For current seasons, if its timestamp is less than a day old, then we consider it valid
     *
     * @param season the season url to scrape
     * @return the valid cached data or nothing
     */
    private Optional<ScrapaData> getCachedDataIfValid(String season) {
        Optional<ScrapaData> validData = Optional.empty();
        Optional<ScrapaData> cachedData = getCachedData(season);
        if(cachedData.isPresent() && isValidCache(season, cachedData.get())){
            validData = cachedData;
        }
        return validData;
    }

    private boolean isValidCache(String season, ScrapaData data) {
        return isFromThePast(season) || isLessThanOneDayOld(data);
    }

    private boolean isLessThanOneDayOld(ScrapaData data) {
        DateTime dataTime = new DateTime(data.getAddedOnUTC().getTime());
        return dataTime.plusDays(1).isAfter(Utils.getCurrentTimeStampUTC().getTime());
    }

    private Optional<ScrapaData> getCachedData(String relativePath) {
        Optional<ScrapaData> scrapaData = Optional.empty();
        try {
            scrapaData = getH2ScrapaData().getScrapaDataWhere(relativePath);
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
        if(h2ScrapaData != null) {
            h2ScrapaData.close();
            closeConnection();
        }
        db.close();
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Exception while closing connection", e);
        }
    }

    private H2ScrapaData getH2ScrapaData() throws SQLException {
        if(h2ScrapaData == null) {
            connection = db.getConnection();
            h2ScrapaData = new H2ScrapaData(connection);
        }
        return h2ScrapaData;
    }
}
