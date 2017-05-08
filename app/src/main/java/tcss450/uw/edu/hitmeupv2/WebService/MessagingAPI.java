package tcss450.uw.edu.hitmeupv2.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * This is the interface we will use to interact with our backend web service
 */

public interface MessagingAPI {
    /** Login
     *
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login-android")
    Call<List<User>> login(@Field("username") String username, @Field("password") String password);
}
