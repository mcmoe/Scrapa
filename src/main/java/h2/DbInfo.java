package h2;

/**
 * Information repo for H2 Database
 * Created by MC on 4/25/2014.
 */
public class DbInfo {
    static final String H2_MEM_DB = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    static final String H2_EMBD_DB = "jdbc:h2:./test;DB_CLOSE_DELAY=-1";
    static final String PASSWORD = "password";
    static final String USER = "user";
}
