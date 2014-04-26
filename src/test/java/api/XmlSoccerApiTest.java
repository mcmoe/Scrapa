package api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Unit Test the XML SOCCER API Demo Trial
 * Created by MC on 4/25/2014.
 */
public class XmlSoccerApiTest {
    private static final String API_KEY = "EUNXZONSOIZIZFEDDGZJMVGYYIWSTZNYKZQVBJWNECYUFUDEDH";

    @Test
    public void test_xml_soccer_api_key() throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL("http://www.xmlsoccer.com/FootballDataDemo.asmx/CheckApiKey?ApiKey=" + API_KEY);
        URLConnection conn = url.openConnection();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(conn.getInputStream());

        Element element = doc.getDocumentElement();
        Node attribute = element.getAttributes().item(0);

        String attributeName = attribute.getNodeName();
        System.err.println(attributeName);
        MatcherAssert.assertThat(attributeName, equalTo("xmlns"));

        String attributeValue = attribute.getNodeValue();
        System.err.println(attributeValue);
        MatcherAssert.assertThat(attributeValue, equalTo("http://xmlsoccer.com/"));

        Node node = element.getFirstChild();
        String response = node.getNodeValue();
        System.err.println(response);
        MatcherAssert.assertThat(response, equalTo("Hello  , you have access to free leagues (demo-user)."));
    }
}
