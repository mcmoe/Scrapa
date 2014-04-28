package commons;

import lombok.Cleanup;

import java.io.*;

import static java.util.stream.Collectors.joining;

/**
 * Project utility class for generic helpers
 * Created by mcmoe on 4/28/2014.
 */
public class Utils {
    public static String getString(InputStream inStream) throws IOException {
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        return readAll(reader);
    }

    public static String getString(Reader reader) throws IOException {
        @Cleanup BufferedReader bufferedReader = new BufferedReader(reader);
        return readAll(bufferedReader);
    }

    private static String readAll(BufferedReader bufferedReader) {
        return bufferedReader.lines().collect(joining(""));
    }
}
