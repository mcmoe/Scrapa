package h2.table;

import h2.connection.H2Utils;
import h2.sql.LeagueStandingSQL;
import lombok.Cleanup;
import model.LeagueStanding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for H2 LEAGUE_STANDING table SQL transactions
 * Created by mcmoe on 4/30/2014.
 */
public class H2LeagueStanding {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2LeagueStanding.class);

    private Statement statement;
    private PreparedStatement addLeagueStandingStatement;

    private Connection connection;

    public H2LeagueStanding(Connection connection) {
        this.connection = connection;
    }

    public void createLeagueStandingTable() throws SQLException {
        getStatement().execute(LeagueStandingSQL.CREATE_LEAGUE_STANDING_TABLE);
    }

    private Statement getStatement() throws SQLException {
        if(statement == null) {
            statement = H2Utils.createStatement(connection);
        }
        return statement;
    }

    private PreparedStatement prepareAddLeagueStandingStatement() throws SQLException {
        if(addLeagueStandingStatement == null) {
            addLeagueStandingStatement = connection.prepareStatement(LeagueStandingSQL.ADD_LEAGUE_STANDING);
        }
        return addLeagueStandingStatement;
    }

    public ResultSet getMetaData() throws SQLException {
        return connection.getMetaData().getColumns(null, null, LeagueStandingSQL.LEAGUE_STANDING_TABLE , null);
    }

    public List<LeagueStanding> getLeagueStandings() throws SQLException {
        @Cleanup ResultSet resultSet = getStatement().executeQuery(LeagueStandingSQL.GET_LEAGUE_STANDINGS);
        List<LeagueStanding> leagueStandings = new ArrayList<>();

        while(resultSet.next()) {
            leagueStandings.add(new LeagueStanding(resultSet.getInt(LeagueStandingSQL.COLUMNS.RANKING.index()),
                                resultSet.getString(LeagueStandingSQL.COLUMNS.TEAM.index())));
        }
        return leagueStandings;
    }

    public int addLeagueStanding(int ranking, String team) throws SQLException {
        PreparedStatement addLeagueStandingStatement = prepareAddLeagueStandingStatement();
        addLeagueStandingStatement.setString(LeagueStandingSQL.COLUMNS.TEAM.index(), team);
        addLeagueStandingStatement.setInt(LeagueStandingSQL.COLUMNS.RANKING.index(), ranking);
        return addLeagueStandingStatement.executeUpdate();
    }

    public int deleteLeagueStandings() throws SQLException {
        return getStatement().executeUpdate(LeagueStandingSQL.DELETE_LEAGUE_STANDINGS);
    }

    public void close() {
        try {
            if(addLeagueStandingStatement != null) {
                addLeagueStandingStatement.close();
            }
            if(statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Exception while closing statements", e);
        }
    }
}
