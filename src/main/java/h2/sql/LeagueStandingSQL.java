package h2.sql;

/**
 * SQL statements for LEAGUE_STANDING Table
 * Created by mcmoe on 4/30/2014.
 */
public class LeagueStandingSQL {
    public static final String LEAGUE_STANDING_TABLE = "LEAGUE_STANDING";
    private static final String RANKING = "RANKING";
    private static final String TEAM = "TEAM";

    public enum COLUMNS {
        RANKING(1),
        TEAM(2);

        private final int index;
        COLUMNS(int index) {
            this.index = index;
        }
        public int index() {
            return index;
        }
    }

    public static final String
            ADD_LEAGUE_STANDING =
            "INSERT INTO " + LEAGUE_STANDING_TABLE + " (" + RANKING + ", " + TEAM + ") VALUES (?,?)";
    public static final String
            CREATE_LEAGUE_STANDING_TABLE =
            "CREATE TABLE IF NOT EXISTS " + LEAGUE_STANDING_TABLE + "("
                    + RANKING + " INT, " + TEAM + " VARCHAR(255), "
                    + "PRIMARY KEY (RANKING, TEAM)"
                    + ")";
    public static final String
            GET_LEAGUE_STANDINGS =
            "SELECT * FROM " + LEAGUE_STANDING_TABLE;
    public static final String
            DELETE_LEAGUE_STANDINGS =
            "DELETE FROM " + LEAGUE_STANDING_TABLE;
}
