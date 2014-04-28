package parser.league;

import model.TeamGoals;
import model.TopScorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import scraper.wrappers.LeagueScraperData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

/**
 * LeagueParser top scorer and team goals information from scraped free-elements data
 * Created by mcmoe on 4/27/2014.
 */
public class LeagueParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeagueParser.class);
    private LeagueScraperData data;
    private Document document;
    private XPath xPath;

    public LeagueParser(LeagueScraperData data) {
        this.data = data;
        xPath = XPathFactory.newInstance().newXPath();
    }

    public void parseAndVisit(TopScorersVisitor topScorersVisitor, TeamGoalsVisitor teamGoalsVisitor) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        visitTopScorers(topScorersVisitor);
        visitTeamGoals(teamGoalsVisitor);
    }

    private void visitTeamGoals(TeamGoalsVisitor teamGoalsVisitor) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        NodeList teamRows = parseTeamGoals();
        for(int i = 0; i < teamRows.getLength(); ++i) {
            visitTeamGoals(teamGoalsVisitor, teamRows.item(i));
        }
        teamGoalsVisitor.onExit();
    }

    private void visitTopScorers(TopScorersVisitor topScorersVisitor) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        NodeList playerRows = parseTopScorers();
        for(int i = 0; i < playerRows.getLength(); ++i) {
            visitTopScorer(topScorersVisitor, playerRows.item(i));
        }
        topScorersVisitor.onExit();
    }

    private NodeList parseTopScorers() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        return filterNodes("/tbody/tr/th [@class='NRW']");
    }

    private NodeList parseTeamGoals() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        return filterNodes("/tbody/tr/td [@class='TMN']");
    }

    private NodeList filterNodes(String expression) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        setupDocument();
        return (NodeList) xPath.evaluate(expression, document, XPathConstants.NODESET);
    }

    private synchronized void setupDocument() throws ParserConfigurationException, SAXException, IOException {
        if(document == null) {
            document = parseTable();
        }
    }

    private Document parseTable() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(data.getXmlTable()));
        return builder.parse(is);
    }

    private void visitTopScorer(TopScorersVisitor topScorersVisitor, Node indexCell) {
        Node playerNameCell = indexCell.getNextSibling();
        Node playerTeamCell = playerNameCell.getNextSibling();
        Node playerGoalsCell = playerTeamCell.getNextSibling();

        TopScorer topScorer = buildTopScorer(cellToString(playerNameCell),
                cellToString(playerTeamCell), cellToString(playerGoalsCell));
        topScorersVisitor.onRow(topScorer);
    }

    private void visitTeamGoals(TeamGoalsVisitor teamGoalsVisitor, Node teamNameCell) {
        Node teamGoalsCell = teamNameCell.getNextSibling();

        TeamGoals teamGoals = buildTeamGoals(cellToString(teamNameCell),
                cellToString(teamGoalsCell));
        teamGoalsVisitor.onRow(teamGoals);
    }

    private static String cellToString(Node textCell) {
        return textCell.getTextContent().trim();
    }

    private static TeamGoals buildTeamGoals(String team, String goals) {
        return new TeamGoals(team, Integer.valueOf(goals));
    }

    private static TopScorer buildTopScorer(String player, String team, String goals) {
        return new TopScorer(player, team, Integer.valueOf(goals));
    }

    /* Only reason i keep this is to illustrate the use of Optional */
    static void logRows(NodeList rows) {
        for(int i = 0; i < rows.getLength(); ++i) {
            Node firstCell = rows.item(i);
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(cellToString(firstCell));

            Optional<Node> nextCell = Optional.ofNullable(firstCell.getNextSibling());
            while(nextCell.isPresent()) {
                String textContent = cellToString(nextCell.get());
                if(!textContent.isEmpty()) {
                    sBuilder.append(",").append(textContent);
                }
                nextCell = Optional.ofNullable(nextCell.get().getNextSibling());
            }
            LOGGER.info(sBuilder.toString());
        }
    }
}