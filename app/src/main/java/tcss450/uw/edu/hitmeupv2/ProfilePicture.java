package tcss450.uw.edu.hitmeupv2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;
import tcss450.uw.edu.hitmeupv2.WebService.User;

/**
 * Created by Shema on 5/18/2017.
 */

public class ProfilePicture{
    /** URL for site */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    private String mUserId;
    private String mUsername;
    private String mProfileImgPath;
    private String mURL;


    public ProfilePicture(String theUserId) {
        mUserId = theUserId;
        retrieveImg();
    }

    private void retrieveImg() {
        final String profileImgPath = null;

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        Call<List<User>> call = api.getUserInfo(mUserId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    System.out.println("here");
                    User me = response.body().get(0);

                    mProfileImgPath = me.getProfileImgPath();
                    mUsername = me.getUsername();
                    mURL = BASE_URL +  "public/" + mProfileImgPath;
                    System.out.println("img url " +  mURL);
                    System.out.println("username " +  mUsername);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });
    }


    public String getProfileURL() {
        return mURL;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getProfileImgPath() {
        return mProfileImgPath;
    }
}
