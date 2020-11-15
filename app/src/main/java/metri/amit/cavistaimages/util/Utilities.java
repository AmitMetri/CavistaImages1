package metri.amit.cavistaimages.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by amitmetri on 11,November,2020
 */
public class Utilities {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                return false;
            }
        } catch (Exception e) {
            String TAG = "Utilities";
            Log.e(TAG, "Error: " + e, e);
        }

        return true;
    }
}
