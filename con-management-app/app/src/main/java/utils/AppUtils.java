package utils;

import java.util.List;

/**
 * Created by samsok on 11/15/15.
 */
public class AppUtils {

    /**
     * @param query a substring to search for conventions
     * @return a list of all conventions fitting the query.
     *          null if an error occurred
     */
    public static List<Convention> searchConventions(String query) {
        throw new RuntimeException("Not Implemented yet");
    }

    /**
     * @param name name of convention to download
     * @effects Will check site for existance of convention (site-side check)
     *          If a convention with that name exists, sends back a list of:
     *              events, hosts, rooms, and documents
     *          It will then go through the list of documents and download each.
     *          It will store everything in a file in the appropriate location on the machine
     */
    public static void downloadConvention(String name) {
        throw new RuntimeException("Not Implemented yet");
    }

    /**
     * @return a list of all conventions whose files are on the machine
     *          ps: checks for existance of JSON file.
     */
    public static List<Convention> getDownloadedConventions() {
        throw new RuntimeException("Not Implemented yet");
    }
}
