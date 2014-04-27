package scrape;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * Scrapes season team goals and top scorers information from free-elements
 * Created by MC on 4/26/2014.
 */
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);
    private static Integer latestPosition;

    public static NodeList parseRows(String tableXml) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        Document doc = parseTable(tableXml);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.evaluate("/tbody/tr/th [@class='NRW']", doc, XPathConstants.NODESET);
    }

    public static String scrapeWeb() {
        String season = "http://www.free-elements.com/England/Seasons/S2012.html";
        //String season = "http://www.free-elements.com/England/S2013.html";
        MechanizeAgent agent = new MechanizeAgent();
        HtmlDocument page = agent.get(season);
        HtmlElement table = page.htmlElements().get(HtmlQueryBuilder.byTag("tbody"));
        return normalizeXml(table.toString());
    }

    public static String normalizeXml(String xml) {
        String oneLineXml = xml.replaceAll("&nbsp;", "");
        List<String> words = new ArrayList<>();
        Collections.addAll(words, oneLineXml.split("\n"));
        return words.stream().map(String::trim).collect(joining(""));
    }

    private static Document parseTable(String tableXml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(tableXml));
        return builder.parse(is);
    }

    public static String cellToString(Node textCell) {
        return textCell.getTextContent().trim();
    }

    private static TopScorer buildTopScorer(String position, String name, String team, String goals) {
        TopScorer topScorer = null;
        try {
            topScorer = new TopScorer(Integer.valueOf(position), name, team, Integer.valueOf(goals));
        } catch (NumberFormatException e) {
            LOGGER.error(" -- " + position + " - " + name + " - " + team + " - " + goals + " -- ", e);
        }

        return topScorer;
    }

    private static TeamGoals buildTeamGoals(Integer position, String team, String goals) {
        return new TeamGoals(position, team, Integer.valueOf(goals));
    }

    static void parseAndVisit(String tableXml, TopScorersVisitor topScorersVisitor, TeamGoalsVisitor teamGoalsVisitor) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        NodeList rows = parseRows(tableXml);
        latestPosition = 1;
        Integer teamPosition = 1;
        for(int i = 0; i < rows.getLength(); ++i) {
            Node positionCell = rows.item(i);
            String position = inheritPositionIfEmpty(positionCell);

            Node delimiterCell = visitTopScorer(topScorersVisitor, positionCell, position);

            if(delimiterCell != null) {
                visitTeamGoals(teamGoalsVisitor, delimiterCell, teamPosition++);
            }
        }
    }

    private static void visitTeamGoals(TeamGoalsVisitor teamGoalsVisitor, Node delimiterCell, Integer position) {
        Node teamNameCell = delimiterCell.getNextSibling();
        Node teamGoalsCell = teamNameCell.getNextSibling();

        TeamGoals teamGoals = buildTeamGoals(position, cellToString(teamNameCell),
                                             cellToString(teamGoalsCell));
        teamGoalsVisitor.visit(teamGoals);
    }

    private static Node visitTopScorer(TopScorersVisitor topScorersVisitor, Node delimiterCell, String position) {
        Node playerNameCell = delimiterCell.getNextSibling();
        Node playerTeamCell = playerNameCell.getNextSibling();
        Node playerGoalsCell = playerTeamCell.getNextSibling();

        TopScorer topScorer = buildTopScorer(position, cellToString(playerNameCell),
                                             cellToString(playerTeamCell), cellToString(playerGoalsCell));
        topScorersVisitor.visit(topScorer);

        return playerGoalsCell.getNextSibling();
    }

    private static String inheritPositionIfEmpty(Node positionCell) {
        String position = cellToString(positionCell);

        if(position.isEmpty()) {
            return latestPosition.toString();
        } else {
            latestPosition = Integer.valueOf(position);
        }
        return position;
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
