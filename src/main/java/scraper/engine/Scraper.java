package scraper.engine;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * Scrapes season team goals and top scorers information from free-elements
 * Created by mcmoe on 4/26/2014.
 */
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private Set<String> relativePaths;
    private Iterator<String> iterator;

    public Scraper(Set<String> relativePaths) {
        this.relativePaths = relativePaths;
        this.iterator = relativePaths.iterator();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String scrapeNext() {
        return scrapeWeb(iterator.next());
    }

    public Set<String> getRelativePaths() {
        return relativePaths;
    }

    private static String scrapeWeb(String relativePath) {
        String season = "http://www.free-elements.com/" + relativePath;
        MechanizeAgent agent = new MechanizeAgent();
        HtmlDocument page = agent.get(season);
        HtmlElement table = page.htmlElements().get(HtmlQueryBuilder.byTag("tbody"));
        String normalizedXml = normalizeXml(table.toString());
        LOGGER.info(normalizedXml);
        return normalizedXml;
    }

    private static String normalizeXml(String xml) {
        String oneLineXml = xml.replaceAll("&nbsp;", "");
        List<String> words = new ArrayList<>();
        Collections.addAll(words, oneLineXml.split("\n"));
        return words.stream().map(String::trim).collect(joining(""));
    }
}
