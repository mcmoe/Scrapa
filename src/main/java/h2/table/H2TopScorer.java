package h2.table;

import h2.connection.H2Utils;
import h2.sql.TopScorerSQL;
import lombok.Cleanup;
import model.TopScorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for H2 TOP_SCORER table SQL transactions
 * Created by mcmoe on 4/26/2014.
 */
public class H2TopScorer {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2TopScorer.class);

    private Connection connection;
    private Statement statement;
    private PreparedStatement addTopScorersStatement;

    public H2TopScorer(Connection connection) {
        this.connection = connection;
    }

    public void createTopScorersTable() throws SQLException {
        Statement statement = getStatement();
        statement.execute(TopScorerSQL.CREATE_TOP_SCORERS_TABLE);
    }

    public ResultSet getMetaData() throws SQLException {
        return connection.getMetaData().getColumns(null, null, TopScorerSQL.TOP_SCORERS_TABLE , null);
    }

    public List<TopScorer> getTopScorers() throws SQLException {
        @Cleanup ResultSet resultSet = getStatement().executeQuery(TopScorerSQL.GET_TOP_SCORERS);
        List<TopScorer> topScorers = new ArrayList<>();

        while(resultSet.next()) {
            topScorers.add(new TopScorer(resultSet.getString(TopScorerSQL.COLUMNS.PLAYER.index()),
                            resultSet.getString(TopScorerSQL.COLUMNS.TEAM.index()),
                            resultSet.getInt(TopScorerSQL.COLUMNS.GOALS.index())));
        }

        return topScorers;
    }

    public int addTopScorer(String player, String team, int goals) throws SQLException {
        PreparedStatement addTopScorerStatement = prepareAddTopScorersStatement();
        addTopScorerStatement.setString(TopScorerSQL.COLUMNS.PLAYER.index(), player);
        addTopScorerStatement.setString(TopScorerSQL.COLUMNS.TEAM.index(), team);
        addTopScorerStatement.setInt(TopScorerSQL.COLUMNS.GOALS.index(), goals);
        return addTopScorerStatement.executeUpdate();
    }

    public int deleteTopScorers() throws SQLException {
        return getStatement().executeUpdate(TopScorerSQL.DELETE_TOP_SCORERS);
    }

    private Statement getStatement() throws SQLException {
        if(statement == null) {
            statement = H2Utils.createStatement(connection);
        }
        return statement;
    }

    private PreparedStatement prepareAddTopScorersStatement() throws SQLException {
        if(addTopScorersStatement == null) {
            addTopScorersStatement = connection.prepareStatement(TopScorerSQL.ADD_TOP_SCORER);
        }
        return addTopScorersStatement;
    }

    public void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (addTopScorersStatement != null) {
                addTopScorersStatement.close();
            }
            connection.close();
        } catch(SQLException e) {
            LOGGER.error("Exception while closing statements", e);
        }
    }
}
