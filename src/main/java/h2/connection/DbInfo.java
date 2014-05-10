package h2.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Information repo for H2 Database
 * Created by mcmoe on 4/25/2014.
 */
public class DbInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbInfo.class);
    static final Properties properties = loadDbProperties();

    static final String H2_MEM_DB = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    static final String H2_EMBD_DB = properties.getProperty("h2.db.file") + "./"
                                   + properties.getProperty("h2.db.url")
                                   + ";DB_CLOSE_DELAY=-1";
    static final String PASSWORD = properties.getProperty("h2.db.password");
    static final String USER = properties.getProperty("h2.db.user");

    /**
     * Load DB properties file from src/main/resources/db.properties
     * This file is also used by flyway to create and migrate the DB
     * @return The map of property key/values
     */
    private static Properties loadDbProperties() {
        Properties props = new Properties();
        try {
            props.load(Files.newBufferedReader(Paths.get("src", "main", "resources", "db.properties")));
        } catch (IOException e) {
            LOGGER.error("could not load db.properties file", e);
        }
        return props;
    }
}
