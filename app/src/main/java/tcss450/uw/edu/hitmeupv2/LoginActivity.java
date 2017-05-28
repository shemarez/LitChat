package tcss450.uw.edu.hitmeupv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
//    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    /** Use this if you want to test on a local server with emulator */
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /** Loading circle, for when server is sleeping. */
    private ProgressDialog mLoadingScreen;
    /** Save users information when logging in.*/
    private SharedPreferences mSharedPreferences;
    /** The username edittext */
    private EditText usernameEditText;
    /** The password edit text */
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoadingScreen = new ProgressDialog(LoginActivity.this);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);

        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), true))  {
            String userId = mSharedPreferences.getString("userId", null);
            System.out.println("USER ID " + userId);
            Intent i = new Intent(this, HomepageActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
            finish();

        }

    }

    /**
     *  When the user has to sign up, switch to register page
     * @param v
     */
    public void switchToSignUp(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Handle what occurs once user clicks login button.
     * Sends info to server, returns whether the login
     * is correct.
     * @param view the view
     */
    public void clickedLogin(View view) {

        final SharedPreferences.Editor edit = mSharedPreferences.edit();

        mLoadingScreen.setTitle("Please Wait");
        mLoadingScreen.setMessage("Loading...");
        mLoadingScreen.setIndeterminate(false);
        mLoadingScreen.setCancelable(true);
        mLoadingScreen.show();

        final Intent intent = new Intent(this, HomepageActivity.class);
        String username = usernameEditText.getText().toString(); //grab username
        String password = passwordEditText.getText().toString(); //grab password

        final View parentLayout = findViewById(R.id.passwordEditText);
        final Snackbar snackbar = Snackbar
                .make(parentLayout, "Please enter a valid username or password", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));


        // check if editText is empty
        if(username.length() == 0 || password.length() == 0) {
            mLoadingScreen.dismiss();
            snackbar.show();
        } else {
            //used to convert JSON to POJO (Plain old java object)
            Gson gson = new GsonBuilder().setLenient().create();



            //Set up retrofit to make our API call
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TEST_URL)
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
                        String userId = user.getUserId();

                        intent.putExtra("userId", userId);
                        edit.putString("userId", userId);
//                        edit.putBoolean("loggedIn", true);
                        edit.putBoolean(getString(R.string.LOGGEDIN), false);
                        edit.commit();

                        System.out.println("MY USER ID " + mSharedPreferences.getString("userId", null));


                        //Was successful
                        if (user.getMessage().equals("Success")) {
                            startActivity(intent); //Switch to new activity
                            mLoadingScreen.dismiss();
                            finish();
                        } else {
                            Log.w("LoginActivity", "Login Error, Please Try Again");
                            mLoadingScreen.dismiss();
                            snackbar.setText("Invalid username or password");
                            snackbar.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    System.out.println("fail");
                    mLoadingScreen.dismiss();
                    snackbar.setText("Something went wrong. Please retry.");
                    snackbar.show();
                    t.printStackTrace();
                }
            });

        }
    }
}
