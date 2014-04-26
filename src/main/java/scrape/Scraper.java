package scrape;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.query.HtmlQueryBuilder;
import org.w3c.dom.Document;
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

import static java.util.stream.Collectors.joining;

/**
 * Scrapes season team goals and top scorers information from free-elements
 * Created by MC on 4/26/2014.
 */
public class Scraper {

    public static NodeList parseRows(String tableXml) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        Document doc = parseTable(tableXml);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.evaluate("/tbody/tr/th [@class='NRW']", doc, XPathConstants.NODESET);
    }

    public static String scrapeWeb() {
        MechanizeAgent agent = new MechanizeAgent();
        HtmlDocument page = agent.get("http://www.free-elements.com/England/S2013.html");
        HtmlElement table = page.htmlElements().get(HtmlQueryBuilder.byTag("tbody"));
        return normalizeXml(table.toString());
    }

    private static String normalizeXml(String xml) {
        List<String> words = new ArrayList<>();
        Collections.addAll(words, xml.split("&nbsp;"));
        return words.stream().map(String::trim).collect(joining(""));
    }

    private static Document parseTable(String tableXml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(tableXml));
        return builder.parse(is);
    }
}
