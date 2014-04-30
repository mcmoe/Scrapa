package h2.table;

import h2.connection.H2Utils;
import h2.sql.TeamGoalsSQL;
import lombok.Cleanup;
import model.TeamGoals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for H2 TEAM_GOALS table SQL transactions
 * Created by mcmoe on 4/28/2014.
 */
public class H2TeamGoals {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2TeamGoals.class);

    private Statement statement;
    private PreparedStatement addTeamGoalsStatement;

    private Connection connection;

    public H2TeamGoals(Connection connection) {
        this.connection = connection;
    }

    public void createTeamGoalsTable() throws SQLException {
        getStatement().execute(TeamGoalsSQL.CREATE_TEAM_GOALS_TABLE);
    }

    private Statement getStatement() throws SQLException {
        if(statement == null) {
            statement = H2Utils.createStatement(connection);
        }
        return statement;
    }

    private PreparedStatement prepareAddTeamGoalsStatement() throws SQLException {
        if(addTeamGoalsStatement == null) {
            addTeamGoalsStatement = connection.prepareStatement(TeamGoalsSQL.ADD_TEAM_GOALS);
        }
        return addTeamGoalsStatement;
    }

    public ResultSet getMetaData() throws SQLException {
        return connection.getMetaData().getColumns(null, null, TeamGoalsSQL.TEAM_GOALS_TABLE , null);
    }

    public List<TeamGoals> getTeamGoals() throws SQLException {
        @Cleanup ResultSet resultSet = getStatement().executeQuery(TeamGoalsSQL.GET_TEAM_GOALS);
        List<TeamGoals> teamGoals = new ArrayList<>();

        while(resultSet.next()) {
            teamGoals.add(new TeamGoals(resultSet.getString(TeamGoalsSQL.COLUMNS.TEAM.index()),
                                        resultSet.getInt(TeamGoalsSQL.COLUMNS.GOALS.index())));
        }
        return teamGoals;
    }

    public int addTeamGoals(String team, int goals) throws SQLException {
        PreparedStatement addTeamGoalsStatement = prepareAddTeamGoalsStatement();
        addTeamGoalsStatement.setString(TeamGoalsSQL.COLUMNS.TEAM.index(), team);
        addTeamGoalsStatement.setInt(TeamGoalsSQL.COLUMNS.GOALS.index(), goals);
        return addTeamGoalsStatement.executeUpdate();
    }

    public int deleteTeamGoals() throws SQLException {
        return getStatement().executeUpdate(TeamGoalsSQL.DELETE_TEAM_GOALS);
    }

    public void close() {
        try {
            if(addTeamGoalsStatement != null) {
                addTeamGoalsStatement.close();
            }
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Exception while closing statements", e);
        }
    }
}
