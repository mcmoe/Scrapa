package h2;

import java.sql.*;

/**
 * Helper class for all H2 related processing on TOP_SCORER table
 * Created by MC on 4/26/2014.
 */
public class H2TopScorer {
    static void createTopScorersTable(Statement statement) throws SQLException {
        statement.execute(TopScorersSQL.CREATE_TOP_SCORERS_TABLE);
    }

    static PreparedStatement prepareAddTopScorersStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(TopScorersSQL.ADD_TOP_SCORER);
    }

    static ResultSet getTopScorers(Statement statement) throws SQLException {
        return statement.executeQuery(TopScorersSQL.GET_TOP_SCORERS);
    }

    static int addTopScorer(PreparedStatement addTopScorerStatement, int position, String player, String team, int goals) throws SQLException {
        addTopScorerStatement.setInt(1, position);
        addTopScorerStatement.setString(2, player);
        addTopScorerStatement.setString(3, team);
        addTopScorerStatement.setInt(4, goals);
        return addTopScorerStatement.executeUpdate();
    }

    private static class TopScorerStatement  {
        private PreparedStatement addTopScorerStatement;
    }
}
