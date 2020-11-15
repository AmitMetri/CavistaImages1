package metri.amit.cavistaimages.db;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import metri.amit.cavistaimages.model.ImageDetails;

/* Helper class which provides singleton DB instance.
 * Exposes function to insert the comments.
 * Exposes function to read the live data from the table */
public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper databaseHelper;
    private final ImageDetailsDao imageDetailsDao;

    private DatabaseHelper(Application application) {
        imageDetailsDao = ImageDetailsRoomDb.getDbInstance(application).getImageDetailsDao();
    }

    /* Use the singleton instance of DB from below getInstance method  */
    public static DatabaseHelper getInstance(Application application) {
        synchronized (DatabaseHelper.class) {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(application);
                Log.d(TAG, "NEW DB OBJECT CREATED");
            }
        }
        return databaseHelper;
    }

    /* Insert comments into table */
    public void insertComment(ImageDetails imageDetails) {
        List<ImageDetails> imageDetails1 = new ArrayList<>();
        imageDetails1.add(imageDetails);
        new Thread(() -> {
            Log.d(TAG, "Inserting comment...");
            imageDetailsDao.insertAll(imageDetails1);
        }).start();
    }

    /* Comment details are fetched from the table */
    public LiveData<List<ImageDetails>> fetchImageDetails(String id) {
        Log.d(TAG, "fetching comments...");
        return imageDetailsDao.fetchAllComments(id);
    }
}
