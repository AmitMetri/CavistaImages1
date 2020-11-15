package metri.amit.cavistaimages.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import metri.amit.cavistaimages.model.ImageDetails;

@Dao
public interface ImageDetailsDao {
    @Query("select * from table_comments WHERE id = :id")
    LiveData<List<ImageDetails>> fetchAllComments(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ImageDetails> imageDetails);
}
