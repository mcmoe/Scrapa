package parser;

import model.TeamGoals;
import model.TopScorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
 * Parser top scorer and team goals information from scraped free-elements data
 * Created by mcmoe on 4/27/2014.
 */
public class Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public static void parseAndVisit(String tableXml, TopScorersVisitor topScorersVisitor, TeamGoalsVisitor teamGoalsVisitor) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        NodeList rows = parseRows(tableXml);
        for(int i = 0; i < rows.getLength(); ++i) {
            Node indexCell = rows.item(i);

            Optional<Node> delimiterCell = visitTopScorer(topScorersVisitor, indexCell);

            if(delimiterCell.isPresent()) {
                visitTeamGoals(teamGoalsVisitor, delimiterCell.get());
            }
        }

        topScorersVisitor.onExit();
        teamGoalsVisitor.onExit();
    }

    private static NodeList parseRows(String tableXml) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        Document doc = parseTable(tableXml);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.evaluate("/tbody/tr/th [@class='NRW']", doc, XPathConstants.NODESET);
    }

    private static Document parseTable(String tableXml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(tableXml));
        return builder.parse(is);
    }

    private static Optional<Node> visitTopScorer(TopScorersVisitor topScorersVisitor, Node delimiterCell) {
        Node playerNameCell = delimiterCell.getNextSibling();
        Node playerTeamCell = playerNameCell.getNextSibling();
        Node playerGoalsCell = playerTeamCell.getNextSibling();

        TopScorer topScorer = buildTopScorer(cellToString(playerNameCell),
                cellToString(playerTeamCell), cellToString(playerGoalsCell));
        topScorersVisitor.onRow(topScorer);

        return Optional.ofNullable(playerGoalsCell.getNextSibling());
    }

    private static void visitTeamGoals(TeamGoalsVisitor teamGoalsVisitor, Node delimiterCell) {
        Node teamNameCell = delimiterCell.getNextSibling();
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
        TopScorer topScorer = null;
        try {
            topScorer = new TopScorer(player, team, Integer.valueOf(goals));
        } catch (NumberFormatException e) {
            LOGGER.error("--" + player + "-" + team + "-" + goals + "--", e);
        }

        return topScorer;
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
