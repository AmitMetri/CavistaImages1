package metri.amit.cavistaimages.services;

import metri.amit.cavistaimages.model.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by amitmetri on 11,November,2020
 */
public interface ImageService {

    /* end point to fetch images based of search.
     * This also supports paging */
    @GET("3/gallery/search/{page}")
    Call<Response> fetchImages(
            @Header("Authorization") String authId, @Path("page") int page, @Query("q") String query );

}
