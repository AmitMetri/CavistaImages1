package metri.amit.cavistaimages.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import metri.amit.cavistaimages.model.Image;
import metri.amit.cavistaimages.repo.ImageRepo;

/**
 * Created by amitmetri on 11,November,2020
 */
public class ImageViewModel extends AndroidViewModel {
    public ImageViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Image>> fetchImages(int page, String query){
        return ImageRepo.getInstance().fetchImages(page, query);
    }

    public LiveData<String> getErrorData(){
        return ImageRepo.getInstance().getErrorData();
    }

}
