package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * Allows user to login to application, and displays signup screen.
 */
public class LoginActivity extends AppCompatActivity {

    /*
     * Production server
     */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    /*
     * Use this if you want to test on a local server with emulator
     */
    private static final String TEST_URL = "http://10.0.2.2:8888/";

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    /**
     *  When the user has to sign up, switch to register page
     * @param v
     */
    public void switchToSignUp(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    /**
     * Handle what occurs once user clicks login button.
     * Sends info to server, returns whether the login
     * is correct.
     * @param view the view
     */
    public void clickedLogin(View view) {
        final Intent intent = new Intent(this, HomepageActivity.class);
        String username = usernameEditText.getText().toString(); //grab username
        String password = passwordEditText.getText().toString(); //grab password

        final View parentLayout = findViewById(R.id.passwordEditText);
        final Snackbar snackbar = Snackbar
                .make(parentLayout, "Please enter a valid username or password", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));


        // check if editText is empty
        if(username.length() == 0 || password.length() == 0) {
            snackbar.show();
        } else {
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
                        System.out.println(user.getUserId());

                        String userId = user.getUserId();

                        intent.putExtra("userId", userId);

                        //Was successful
                        if (user.getMessage().equals("Success")) {
                            startActivity(intent); //Switch to new activity
                        } else {
                            Log.w("LoginActivity", "Login Error, Please Try Again");
                            snackbar.setText("Invalid username or password");

                            snackbar.show();
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
}
