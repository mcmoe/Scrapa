package h2.sql;

/**
 * SQL statements for TOP_SCORERS Table
 * Created by mcmoe on 4/25/2014.
 */
public class TopScorerSQL {

    public static final String TOP_SCORERS_TABLE = "TOP_SCORERS";
    private static final String PLAYER = "PLAYER";
    private static final String TEAM = "TEAM";
    private static final String GOALS = "GOALS";

    public enum COLUMNS {
        PLAYER(1),
        TEAM(2),
        GOALS(3);

        private final int index;
        COLUMNS(int index) {
            this.index = index;
        }
        public int index() {
            return index;
        }
    }

    public static final String
            ADD_TOP_SCORER =
            "INSERT INTO " + TOP_SCORERS_TABLE + " (" + PLAYER + ", " + TEAM + ", " + GOALS + ") VALUES (?,?,?)";
    public static final String
            CREATE_TOP_SCORERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TOP_SCORERS_TABLE + "("
                    + PLAYER + " VARCHAR(255), " + TEAM + " VARCHAR(255), " + GOALS + " INT, "
                    + "PRIMARY KEY (PLAYER, TEAM)"
                    + ")";
    public static final String
            GET_TOP_SCORERS =
            "SELECT * FROM " + TOP_SCORERS_TABLE;
    public static final String
            DELETE_TOP_SCORERS =
            "DELETE FROM " + TOP_SCORERS_TABLE;
}
