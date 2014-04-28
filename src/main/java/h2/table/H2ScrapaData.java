package h2.table;

import commons.Utils;
import h2.connection.H2Utils;
import h2.sql.ScrapaDataSQL;
import lombok.Cleanup;
import model.ScrapaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * Created by mcmoe on 4/28/2014.
 */
public class H2ScrapaData {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2ScrapaData.class);

    public static void createScrapaDataTable(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        statement.execute(ScrapaDataSQL.CREATE_SCRAPA_DATA_TABLE);
    }

    private static PreparedStatement prepareAddScrapaDataStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(ScrapaDataSQL.ADD_SCRAPA_URL_DATA);
    }

    static ResultSet getScrapaData(Statement statement) throws SQLException {
        return statement.executeQuery(ScrapaDataSQL.GET_SCRAPA_DATA);
    }

    private static PreparedStatement prepareGetScrapaDataWhereStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(ScrapaDataSQL.GET_SCRAPA_DATA_WHERE);
    }

    public static Optional<ScrapaData> getScrapaDataWhere(Connection connection, String url) throws SQLException {
        PreparedStatement statement = prepareGetScrapaDataWhereStatement(connection);
        statement.setString(ScrapaDataSQL.COLUMNS.URL.index(), url);
        ResultSet resultSet = statement.executeQuery();
        Optional<ScrapaData> scrapaData = Optional.empty();

        if(resultSet.next()) {
            Reader characterStream = resultSet.getCharacterStream(ScrapaDataSQL.COLUMNS.DATA.index());
            String data = getString(characterStream);
            scrapaData = Optional.of(new ScrapaData(url, data));
        }

        return scrapaData;
    }

    private static String getString(Reader characterStream) {
        try {
            return Utils.getString(characterStream);
        } catch (IOException e) {
            LOGGER.error("failed to read xml data from SCRAP_DATA!", e);
        }

        return "";
    }

    public static List<ScrapaData> getScrapaData(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        @Cleanup ResultSet resultSet = statement.executeQuery(ScrapaDataSQL.GET_SCRAPA_DATA);
        List<ScrapaData> scrapaData = new ArrayList<>();

        while (resultSet.next()) {
            Reader characterStream = resultSet.getCharacterStream(ScrapaDataSQL.COLUMNS.DATA.index());
            String string = resultSet.getString(ScrapaDataSQL.COLUMNS.URL.index());
            scrapaData.add(new ScrapaData(string,getString(characterStream)));
        }

        return scrapaData;
    }

    public static int addScrapaData(Connection connection, String url, String data) throws SQLException {
        @Cleanup PreparedStatement addScrapaDataStatement = H2ScrapaData.prepareAddScrapaDataStatement(connection);
        addScrapaDataStatement.setString(ScrapaDataSQL.COLUMNS.URL.index(), url);
        addScrapaDataStatement.setCharacterStream(ScrapaDataSQL.COLUMNS.DATA.index(), new StringReader(data));
        return addScrapaDataStatement.executeUpdate();
    }

    public static int deleteScrapaData(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        return statement.executeUpdate(ScrapaDataSQL.DELETE_SCRAPA_DATA);
    }
}
