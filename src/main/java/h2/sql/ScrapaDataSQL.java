package h2.sql;

/**
 * SQL statements for SCRAPA_DATA Table
 * Created by mcmoe on 4/28/2014.
 */
public class ScrapaDataSQL {

    private static final String SCRAPA_DATA = "SCRAPA_DATA";
    private static final String URL = "URL";
    private static final String DATA = "DATA";

    public enum COLUMNS {
        URL(1),
        DATA(2);

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
            "INSERT INTO " + SCRAPA_DATA + " (" + URL + ", " + DATA + ") VALUES (?,?)";
    public static final String
            CREATE_SCRAPA_DATA_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SCRAPA_DATA + "("
                    + URL + " VARCHAR(255), " + DATA + " CLOB, "
                    + "PRIMARY KEY (URL)"
                    + ")";
    public static final String
            GET_SCRAPA_DATA =
            "SELECT * FROM " + SCRAPA_DATA;
    public static final String
            GET_SCRAPA_DATA_WHERE =
            "SELECT * FROM " + SCRAPA_DATA + " WHERE " + URL + " = ?";
    public static final String
            DELETE_SCRAPA_DATA =
            "DELETE FROM " + SCRAPA_DATA;
}

