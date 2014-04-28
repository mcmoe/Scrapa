package h2.table;

import h2.connection.H2Utils;
import h2.sql.TopScorerSQL;
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
        statement.execute(TopScorerSQL.CREATE_TOP_SCORERS_TABLE);
    }

    private static PreparedStatement prepareAddTopScorersStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(TopScorerSQL.ADD_TOP_SCORER);
    }

    static ResultSet getTopScorers(Statement statement) throws SQLException {
        return statement.executeQuery(TopScorerSQL.GET_TOP_SCORERS);
    }

    public static List<TopScorer> getTopScorers(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        @Cleanup ResultSet resultSet = statement.executeQuery(TopScorerSQL.GET_TOP_SCORERS);
        List<TopScorer> topScorers = new ArrayList<>();

        while(resultSet.next()) {
            topScorers.add(new TopScorer(resultSet.getString(TopScorerSQL.COLUMNS.PLAYER.index()),
                            resultSet.getString(TopScorerSQL.COLUMNS.TEAM.index()),
                            resultSet.getInt(TopScorerSQL.COLUMNS.GOALS.index())));
        }

        return topScorers;
    }

    public static int addTopScorer(Connection connection, String player, String team, int goals) throws SQLException {
        @Cleanup PreparedStatement addTopScorerStatement = H2TopScorer.prepareAddTopScorersStatement(connection);
        addTopScorerStatement.setString(TopScorerSQL.COLUMNS.PLAYER.index(), player);
        addTopScorerStatement.setString(TopScorerSQL.COLUMNS.TEAM.index(), team);
        addTopScorerStatement.setInt(TopScorerSQL.COLUMNS.GOALS.index(), goals);
        return addTopScorerStatement.executeUpdate();
    }

    public static int deleteTopScorers(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        return statement.executeUpdate(TopScorerSQL.DELETE_TOP_SCORERS);
    }
}
