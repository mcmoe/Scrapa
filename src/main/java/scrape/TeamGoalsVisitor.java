package scrape;

import model.TeamGoals;

/**
 * Functional interface to be used as call back,
 * The Scraper class will accept implementations
 * allowing visits per scraped row
 * Created by mcmoe on 4/26/2014.
 */
public interface TeamGoalsVisitor {
    void visit(TeamGoals teamGoals);
}
