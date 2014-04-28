package h2;

import h2.sql.TeamGoalsSQL;
import lombok.Cleanup;
import model.TeamGoals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for all H2 related processing on TEAM_GOALS table
 * Created by mcmoe on 4/28/2014.
 */
public class H2TeamGoals {
    static void createTeamGoalsTable(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        statement.execute(TeamGoalsSQL.CREATE_TEAM_GOALS_TABLE);
    }

    static PreparedStatement prepareAddTeamGoalsStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(TeamGoalsSQL.ADD_TEAM_GOALS);
    }

    static ResultSet getTeamGoals(Statement statement) throws SQLException {
        return statement.executeQuery(TeamGoalsSQL.GET_TEAM_GOALS);
    }

    public static List<TeamGoals> getTeamGoals(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        ResultSet resultSet = statement.executeQuery(TeamGoalsSQL.GET_TEAM_GOALS);
        List<TeamGoals> teamGoals = new ArrayList<>();

        while(resultSet.next()) {
            teamGoals.add(new TeamGoals(resultSet.getString(TeamGoalsSQL.COLUMNS.TEAM.index()),
                                        resultSet.getInt(TeamGoalsSQL.COLUMNS.GOALS.index())));
        }

        return teamGoals;
    }

    static int addTeamGoals(Connection connection, String team, int goals) throws SQLException {
        @Cleanup PreparedStatement addTeamGoalsStatement = H2TeamGoals.prepareAddTeamGoalsStatement(connection);
        addTeamGoalsStatement.setString(TeamGoalsSQL.COLUMNS.TEAM.index(), team);
        addTeamGoalsStatement.setInt(TeamGoalsSQL.COLUMNS.GOALS.index(), goals);
        return addTeamGoalsStatement.executeUpdate();
    }

    static int deleteTeamGoals(Connection connection) throws SQLException {
        @Cleanup Statement statement = H2Utils.createStatement(connection);
        return statement.executeUpdate(TeamGoalsSQL.DELETE_TEAM_GOALS);
    }
}
