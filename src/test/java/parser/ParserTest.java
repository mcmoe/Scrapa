package parser;

import lombok.Cleanup;
import model.TeamGoals;
import model.TopScorer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

/**
 * Unit test the parsing of the scraped free-elements data using mocks
 * The mocks were built from output returned by launching the scraping manually
 * Created by mcmoe on 4/27/2014.
 */
public class ParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserTest.class);
    @Test
    public void test_parse_and_visit_mock_2012() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String tableXml = scrapeMock("scraped2012Table");
        LOGGER.info(tableXml);
        Parser parser = new Parser(tableXml);
        parser.parseAndVisit(new TopScorersVisitorTest(20), new TeamGoalsVisitorTest(20));
    }

    @Test
    public void test_parse_and_visit_mock_2013() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String tableXml = scrapeMock("scraped2013Table");
        LOGGER.info(tableXml);
        Parser parser = new Parser(tableXml);
        parser.parseAndVisit(new TopScorersVisitorTest(21), new TeamGoalsVisitorTest(20));
    }

    @Test
    public void test_parse_and_visit_mock_1986() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String tableXml = scrapeMock("scraped1986Table");
        LOGGER.info(tableXml);
        Parser parser = new Parser(tableXml);
        parser.parseAndVisit(new TopScorersVisitorTest(20), new TeamGoalsVisitorTest(22));
    }

    @Test(expected = NullPointerException.class)
    public void s() {
        Parser.logRows(null);
    }

    private String scrapeMock(String scrapedTable) throws IOException {
        @Cleanup InputStream inStream = getClass().getResourceAsStream(scrapedTable);
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        return reader.lines().collect(joining(""));
    }

    private class TopScorersVisitorTest implements TopScorersVisitor {
        private final int expectedVisits;
        /* using list to preserve order */
        private List<TopScorer> all = new ArrayList<>();

        public TopScorersVisitorTest(int expectedVisits) {
            this.expectedVisits = expectedVisits;
        }

        @Override
        public void onRow(TopScorer topScorer) {
            all.add(topScorer);
        }

        @Override
        public void onExit() {
            int topScorerVisits = all.size();
            assertEquals("Expecting " + expectedVisits + " top scorers!", expectedVisits, topScorerVisits);

            for(TopScorer t : all) {
                LOGGER.info(t.toString());
            }
            // or addTopScorersRow(playerName, playerTeam, playerGoals) :)
        }
    }

    private class TeamGoalsVisitorTest implements TeamGoalsVisitor {
        private final int expectedVisits;
        /* using list to preserve order */
        private List<TeamGoals> all = new ArrayList<>();

        public TeamGoalsVisitorTest(int expectedVisits) {
            this.expectedVisits = expectedVisits;
        }

        @Override
        public void onRow(TeamGoals teamGoals) {
            all.add(teamGoals);
        }

        @Override
        public void onExit() {
            int teamGoalsVisits = all.size();
            assertEquals("Expecting " + expectedVisits + " teams!", expectedVisits, teamGoalsVisits);
            for(TeamGoals t : all) {
                LOGGER.info(t.toString());
            }
            // or addTeamGoalsRow (teamName, teamGoals) :)
        }
    }
}
