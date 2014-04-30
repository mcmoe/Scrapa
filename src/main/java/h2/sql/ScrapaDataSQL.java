package h2.sql;

/**
 * SQL statements for SCRAPA_DATA Table
 * Created by mcmoe on 4/28/2014.
 */
public class ScrapaDataSQL {

    public static final String SCRAPA_DATA_TABLE = "SCRAPA_DATA";
    private static final String URL = "URL";
    private static final String DATA = "DATA";
    private static final String ADDED_ON_UTC = "ADDED_ON_UTC";

    public enum COLUMNS {
        URL(1),
        DATA(2),
        ADDED_ON_UTC(3);
        private final int index;
        COLUMNS(int index) {
            this.index = index;
        }
        public int index() {
            return index;
        }
    }
    public static final String
            ADD_SCRAPA_URL_DATA =
            "INSERT INTO " + SCRAPA_DATA_TABLE + " (" + URL + ", " + DATA + ", " + ADDED_ON_UTC + ") VALUES (?,?,?)";

    public static final String
            MERGE_SCRAPA_URL_DATA =
            "MERGE INTO " + SCRAPA_DATA_TABLE + " (" + URL + ", " + DATA + ", " + ADDED_ON_UTC + ") VALUES (?,?,?)";
    public static final String
            CREATE_SCRAPA_DATA_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SCRAPA_DATA_TABLE + "("
                    + URL + " VARCHAR(255), " + DATA + " CLOB, "
                    + ADDED_ON_UTC + " TIMESTAMP, "
                    + "PRIMARY KEY (URL)"
                    + ")";
    public static final String
            GET_SCRAPA_DATA =
            "SELECT * FROM " + SCRAPA_DATA_TABLE;
    public static final String
            GET_SCRAPA_DATA_WHERE =
            "SELECT * FROM " + SCRAPA_DATA_TABLE + " WHERE " + URL + " = ?";
    public static final String
            DELETE_SCRAPA_DATA =
            "DELETE FROM " + SCRAPA_DATA_TABLE;
}

