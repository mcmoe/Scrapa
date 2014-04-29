package h2.sql;

/**
 * SQL statements for TEAM_GOALS Table
 * Created by mcmoe on 4/28/2014.
 */
public class TeamGoalsSQL {

    public static final String TEAM_GOALS_TABLE = "TEAM_GOALS";
    private static final String TEAM = "TEAM";
    private static final String GOALS = "GOALS";

    public enum COLUMNS {
        TEAM(1),
        GOALS(2);

        private final int index;
        COLUMNS(int index) {
            this.index = index;
        }
        public int index() {
            return index;
        }
    }

    public static final String
            ADD_TEAM_GOALS =
            "INSERT INTO " + TEAM_GOALS_TABLE + " (" + TEAM + ", " + GOALS + ") VALUES (?,?)";
    public static final String
            CREATE_TEAM_GOALS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TEAM_GOALS_TABLE + "("
                    + TEAM + " VARCHAR(255), " + GOALS + " INT, "
                    + "PRIMARY KEY (TEAM)"
                    + ")";
    public static final String
            GET_TEAM_GOALS =
            "SELECT * FROM " + TEAM_GOALS_TABLE;
    public static final String
            DELETE_TEAM_GOALS =
            "DELETE FROM " + TEAM_GOALS_TABLE;
}
