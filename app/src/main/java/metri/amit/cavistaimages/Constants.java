package metri.amit.cavistaimages;

/**
 * Created by amitmetri on 11,November,2020
 */
public class Constants {

    private static final String authValue = "Client-ID 137cda6b5008a7c";

    public static String getAuthValue() {
        return authValue;
    }

    /* Name of the Room DB */
    public static final String DB_IMAGE_DETAILS = "db-image-details";

    /* In cas eof schema change,
    * increase the DB version here */
    public static final int DB_VERSION = 1;

    /* Name of the table used to
    * save comment details */
    public static final String TABLE_COMMENTS = "table_comments";

    /* Base url */
    public static final String baseUrl = "https://api.imgur.com/";

    public static final String EXTRA_IMAGE_DATA = "ImageData";
    public static final String EXTRA_IMAGE_LIST = "ImageList";
}
