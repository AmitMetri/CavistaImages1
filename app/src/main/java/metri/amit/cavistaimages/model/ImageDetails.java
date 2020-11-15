package metri.amit.cavistaimages.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static metri.amit.cavistaimages.Constants.TABLE_COMMENTS;

/**
 * Created by amitmetri on 14,November,2020
 */
@Entity(tableName = TABLE_COMMENTS)
public class ImageDetails implements Serializable {

    private String id;
    private String comment;
    @PrimaryKey
    private long dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

}
