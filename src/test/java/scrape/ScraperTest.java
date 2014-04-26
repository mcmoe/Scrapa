package scrape;

import lombok.Cleanup;
import org.junit.Ignore;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.XPathExpressionException;

import java.io.*;
import java.util.Optional;

import static java.util.stream.Collectors.joining;


/**
 * Attempt to load web page and scrape for data
 * Created by MC on 4/19/2014.
 */
public class ScraperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperTest.class);

    @Test @Ignore /* ignored to avoid spamming the web server! */
    public void test_get_web_page_and_scrap() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String tableXml = Scraper.scrapeWeb();
        NodeList rows = Scraper.parseRows(tableXml);
        saveRows(rows);
    }

    @Test
    public void test_get_mock_page_and_scrap() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String tableXml = scrapeMock();
        NodeList rows = Scraper.parseRows(tableXml);
        saveRows(rows);
    }

    @Test(expected = NullPointerException.class)
    public void dummy_test_print_rows_with_null() {
        logRows(null);
    }

    private void saveRows(NodeList rows) {
        for(int i = 0; i < rows.getLength(); ++i) {

            Node positionCell = rows.item(i);

            Node playerNameCell = positionCell.getNextSibling();
            Node playerTeamCell = playerNameCell.getNextSibling();
            Node playerGoalsCell = playerTeamCell.getNextSibling();

            // addTopScorersRow(position, playerName, playerTeam, playerGoals)
            LOGGER.info(Scraper.cellToString(positionCell) + ","
                      + Scraper.cellToString(playerNameCell) + ","
                      + Scraper.cellToString(playerTeamCell) + ","
                      + Scraper.cellToString(playerGoalsCell));

            Node delimiterCell = playerGoalsCell.getNextSibling();

            Node teamNameCell = delimiterCell.getNextSibling();
            Node teamGoalsCell = teamNameCell.getNextSibling();

            // addTeamsRow (position, teamNAme, teamGoals)
            LOGGER.info(Scraper.cellToString(positionCell) + ","
                      + Scraper.cellToString(teamNameCell) + ","
                      + Scraper.cellToString(teamGoalsCell));
        }
    }

    /* Only reason i keep this is to illustrate the use of Optional */
    private static void logRows(NodeList rows) {
        for(int i = 0; i < rows.getLength(); ++i) {
            Node firstCell = rows.item(i);
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(Scraper.cellToString(firstCell));

            Optional<Node> nextCell = Optional.ofNullable(firstCell.getNextSibling());
            while(nextCell.isPresent()) {
                String textContent = Scraper.cellToString(nextCell.get());
                if(!textContent.isEmpty()) {
                    sBuilder.append(",").append(textContent);
                }
                nextCell = Optional.ofNullable(nextCell.get().getNextSibling());
            }
            LOGGER.info(sBuilder.toString());
        }
    }

    private String scrapeMock() throws IOException {
        @Cleanup InputStream inStream = getClass().getResourceAsStream("scrapedTable.xml");
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        String tableXml = reader.lines().collect(joining("&nbsp;"));
        return Scraper.normalizeXml(tableXml);
    }
}
