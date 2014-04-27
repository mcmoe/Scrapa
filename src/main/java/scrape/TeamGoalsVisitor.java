package scrape;

import model.TeamGoals;

/**
 * Interface to be used as call back.
 * The Scraper class will accept implementations
 * allowing visits per scraped row
 * Finally, the visitor is called with onExit
 * when the scraping is complete
 * Created by mcmoe on 4/26/2014.
 */
public interface TeamGoalsVisitor {
    void onRow(TeamGoals teamGoals);
    void onExit();
}
