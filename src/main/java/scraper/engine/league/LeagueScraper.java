package scraper.engine.league;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scraper.wrapper.LeagueScraperData;

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
    private static MechanizeAgent mechanizeAgent;

    public LeagueScraper(Set<String> relativePaths) {
        this.relativePaths = relativePaths;
        this.iterator = relativePaths.iterator();
        mechanizeAgent = new MechanizeAgent();
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

    private static LeagueScraperData scrapeWeb(String relativePath) {
        String season = HTTP_DOMAIN + relativePath;
        HtmlDocument page = mechanizeAgent.get(season);
        HtmlElement table = page.htmlElements().get(HtmlQueryBuilder.byTag("tbody"));
        String normalizedXml = normalizeXml(table.toString());
        LOGGER.info(normalizedXml);
        return new LeagueScraperData(season, normalizedXml);
    }

    private static String normalizeXml(String xml) {
        String oneLineXml = xml.replaceAll("&nbsp;", "");
        List<String> words = new ArrayList<>();
        Collections.addAll(words, oneLineXml.split("\n"));
        return words.stream().map(String::trim).collect(joining(""));
    }
}
