package h2;

import lombok.Cleanup;
import model.TopScorer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for all H2 related processing on TOP_SCORER table
 * Created by mcmoe on 4/26/2014.
 */
public class H2TopScorer {
    static void createTopScorersTable(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        statement.execute(TopScorersSQL.CREATE_TOP_SCORERS_TABLE);
    }

    static PreparedStatement prepareAddTopScorersStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(TopScorersSQL.ADD_TOP_SCORER);
    }

    static ResultSet getTopScorers(Statement statement) throws SQLException {
        return statement.executeQuery(TopScorersSQL.GET_TOP_SCORERS);
    }

    public static List<TopScorer> getTopScorers(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        ResultSet resultSet = statement.executeQuery(TopScorersSQL.GET_TOP_SCORERS);
        List<TopScorer> topScorers = new ArrayList<>();

        while(resultSet.next()) {
            topScorers.add(new TopScorer(resultSet.getString(1),
                            resultSet.getString(2), resultSet.getInt(3)));
        }

        return topScorers;
    }

    static int addTopScorer(Connection connection, String player, String team, int goals) throws SQLException {
        @Cleanup PreparedStatement addTopScorerStatement = H2TopScorer.prepareAddTopScorersStatement(connection);
        addTopScorerStatement.setString(1, player);
        addTopScorerStatement.setString(2, team);
        addTopScorerStatement.setInt(3, goals);
        return addTopScorerStatement.executeUpdate();
    }

    static int deleteTopScorers(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        return statement.executeUpdate(TopScorersSQL.DELETE_TOP_SCORERS);
    }
}
