package parser.league;

import model.LeagueStanding;

/**
 * Interface to be used as call back.
 * The LeagueScraper class will accept implementations
 * allowing visits per scraped row
 * Finally, the visitor is called with onExit
 * when the scraping is complete
 * Created by mcmoe on 4/28/2014.
 */
public interface LeagueStandingVisitor {
    void onRow(LeagueStanding leagueStanding);
    void onExit();
}
