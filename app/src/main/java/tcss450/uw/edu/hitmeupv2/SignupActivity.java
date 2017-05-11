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
 * This class corresponds to the Sign Up screen in the application.
 * Register using a username and password.
 */

public class SignupActivity extends AppCompatActivity {
    /*
    * Production server
    */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    /*
   * Use this if you want to test on a local server with emulator
   */
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /** Store username.*/
    private String username;
    /** Store password. */
    private String password;
    /** Store username from edittext.*/
    private EditText usernameEditText;
    /** Store password from edittext.*/
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }


    public void switchToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }

    /**
     * Event handler attached to sign up button. Handles what
     * will occur once the sign up is clicked. In this case, it will
     * talk to the web server and insert the information.
     * @param v the view
     */
    public void clickedSignUp(View v) {
        final Intent intent = new Intent(this, LoginActivity.class);
        username = usernameEditText.getText().toString(); //grab username
        password = passwordEditText.getText().toString(); //grab password
        Log.i("Username", username);
        Log.i("Password", password);
        View parentLayout = findViewById(R.id.passwordEditText);
        if(username.length() == 0 || password.length() == 0) {
            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Please enter a valid username or password", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));

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
            Call<List<User>> call = api.register(username, password);

//        //Make API call, handle success and error
            call.enqueue(new Callback<List<User>>() {

                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {

                        Log.i("SignupActivity", response.toString());
                        startActivity(intent); //Switch to new activity
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
