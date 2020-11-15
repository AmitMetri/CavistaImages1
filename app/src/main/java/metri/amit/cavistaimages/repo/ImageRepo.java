package metri.amit.cavistaimages.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import metri.amit.cavistaimages.Constants;
import metri.amit.cavistaimages.model.Datum;
import metri.amit.cavistaimages.model.Image;
import metri.amit.cavistaimages.model.Response;
import metri.amit.cavistaimages.services.ImageService;
import metri.amit.cavistaimages.util.SingleMutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amitmetri on 11,November,2020
 * This is repository class and includes
 * network calls,
 * business logic,
 * validations for network responses,
 * process the network response
 */
public class ImageRepo {

    private static final String TAG = "ImageRepo";
    private static ImageRepo instance;
    private final SingleMutableLiveData<String> errorData = new SingleMutableLiveData<>();

    //Singleton instance of the repository to access repository functions
    public static synchronized ImageRepo getInstance() {
        if (instance == null)
            instance = new ImageRepo();
        return instance;
    }

    /* Returns live data object of response to the ViewModel */
    public MutableLiveData<List<Image>> fetchImages(int page, String query) {
        MutableLiveData<List<Image>> responseMutableLiveData = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageService imageService = retrofit.create(ImageService.class);
        Call<Response> call = imageService.fetchImages(Constants.getAuthValue(), page, query);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NotNull Call<Response> call, @NotNull retrofit2.Response<Response> response) {
                if (validateResponse(response)) {
                    List<Image> imageList = processData(response);
                    if(!imageList.isEmpty())
                        responseMutableLiveData.setValue(imageList);
                    else
                        errorData.setValue("Sorry! we could not find anything on " + query);
                } else {
                    errorData.setValue("Sorry! we are facing technical issues.");
                }
            }

            @Override
            public void onFailure(@NotNull Call<Response> call, @NotNull Throwable t) {
                Log.e(TAG, "Error" + t.getMessage());
                errorData.setValue("Sorry! could not connect to server.");
            }
        });
        return responseMutableLiveData;
    }

    /* Process the network response */
    private List<Image> processData(retrofit2.Response<Response> response) {
        List<Image> imageList = new ArrayList<>();
        try{
            if (response.body() != null) {
                for (Datum datum : Objects.requireNonNull(response.body().getData())) {
                    if (datum.getImages() != null && !datum.getImages().isEmpty()) {
                        for (Image image : datum.getImages()) {
                            if (Objects.requireNonNull(image.getType()).contentEquals("image/jpeg") || image.getType().contentEquals("image/png")) {
                                image.setTitle(datum.getTitle());
                                imageList.add(image);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            Log.e(TAG, "Error: " +e, e);
        }

        return imageList;
    }

    /* Basic validation of response received from network call */
    private boolean validateResponse(retrofit2.Response<Response> response) {
        if (response.isSuccessful() && response.body() != null) {
            return 200 == response.body().getStatus() && response.body().getSuccess();
        }
        return false;
    }

    /* Error scenarios also being observed with
     * LiveData. In case of network call failures, timeouts,
     * this function will emit the error to ViewModel */
    public MutableLiveData<String> getErrorData() {
        return errorData;
    }

}
