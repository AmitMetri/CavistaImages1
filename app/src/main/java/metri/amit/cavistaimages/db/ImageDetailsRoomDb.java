package metri.amit.cavistaimages.db;

import android.app.Application;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import metri.amit.cavistaimages.model.ImageDetails;

import static metri.amit.cavistaimages.Constants.DB_IMAGE_DETAILS;
import static metri.amit.cavistaimages.Constants.DB_VERSION;

/**
 * Created by amit.metri on 11/14/2020
 */
@Database(entities = {ImageDetails.class}, version = DB_VERSION, exportSchema = false)
public abstract class ImageDetailsRoomDb extends RoomDatabase {

    private static final String TAG = "ImageDetailsRoomDb";
    private static ImageDetailsRoomDb dbInstance;

    static synchronized ImageDetailsRoomDb getDbInstance(Application application) {
        if (dbInstance == null) {
            try {
                dbInstance = Room.databaseBuilder(application, ImageDetailsRoomDb.class, DB_IMAGE_DETAILS)
                        .fallbackToDestructiveMigration()
                        .build();
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e, e);
            }
        }
        return dbInstance;
    }

    public abstract ImageDetailsDao getImageDetailsDao();

}


