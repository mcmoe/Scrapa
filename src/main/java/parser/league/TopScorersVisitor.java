package parser.league;

import model.TopScorer;

/**
 * Interface to be used as call back.
 * The LeagueScraper class will accept implementations
 * allowing visits per scraped row
 * Finally, the visitor is called with onExit
 * when the scraping is complete
 * Created by mcmoe on 4/26/2014.
 */
public interface TopScorersVisitor {
    void onRow(TopScorer topScorer);
    void onExit();
}
