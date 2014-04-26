package scrape;

import model.TopScorer;

/**
 * Functional interface to be used as call back,
 * The Scraper class will accept implementations
 * allowing visits per scraped row
 * Created by MC on 4/26/2014.
 */
public interface TopScorersVisitor {
    void visit(TopScorer topScorer);
}
