package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

public class LoginActivity extends AppCompatActivity {

    /*
     * Production server
     */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    /*
     * Use this if you want to test on a local server with emulator
     */
    private static final String TEST_URL = "http://10.0.2.2:8888/";

    private String username;
    private String password;

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    public void clickedSignUp(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void clickedLogin(View view) {
        final Intent intent = new Intent(this, HomepageActivity.class);
        username = usernameEditText.getText().toString(); //grab username
        password = passwordEditText.getText().toString(); //grab password

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        //Call the login interface that we created
        Call<List<User>> call = api.login(username, password);

        //Make API call, handle success and error
        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    User user = response.body().get(0);
                    System.out.println(user.getUsername());
                    System.out.println(user.getPassword());
                    System.out.println(user.getMessage());

                    //Was successful
                    if (user.getMessage().equals("Success")) {
                        startActivity(intent); //Switch to new activity
                    } else {
                        //TODO: Handle error when logging in. Pop up a toast?
                        Log.w("LoginActivity", "Login Error, Please Try Again");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });
    }
}
