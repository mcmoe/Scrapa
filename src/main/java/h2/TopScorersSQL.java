package h2;

/**
 * SQL statements for TOP_SCORERS Table
 * Created by MC on 4/25/2014.
 */
public class TopScorersSQL {

    private static final String PLAYER = "PLAYER";

    private static final String RANK = "RANK";
    private static final String TEAM = "TEAM";
    private static final String GOALS = "GOALS";
    private static final String TOP_SCORERS = "TOP_SCORERS";
    static final String
            ADD_TOP_SCORER =
            "INSERT INTO " + TOP_SCORERS + " (" + RANK + ", " + PLAYER + ", " + TEAM + ", " + GOALS + ") VALUES (?,?,?,?)";
    static final String
            CREATE_TOP_SCORERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TOP_SCORERS + "(" + RANK + " INT PRIMARY KEY, " + PLAYER + " VARCHAR(255), " + TEAM + " VARCHAR(255), " + GOALS + " INT)";
    static final String
            GET_TOP_SCORERS =
            "SELECT * FROM " + TOP_SCORERS;
    static final String
            DELETE_TOP_SCORERS =
            "DELETE FROM " + TOP_SCORERS;
}
