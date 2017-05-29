package tcss450.uw.edu.hitmeupv2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    /**
     * Checking the permission.
     */
    private static final int MY_PERMISSIONS_REQUEST_SMS = 1;
    /*
    * Production server
    */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    /*
   * Use this if you want to test on a local server with emulator
   */
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /**
     * Store username.
     */
    private String username;
    /**
     * Store password.
     */
    private String password;
    /**
     * Store username from edittext.
     */
    private EditText usernameEditText;
    /**
     * Store password from edittext.
     */
    private EditText passwordEditText;
    /**
     * Store confirm password from edittext.
     */
    private EditText confirmPassEditText;
    /**
     * Store name from edittext.
     */
    private EditText nameEditText;
    /**
     * Store phone from edittext.
     */
    private EditText phoneEditText;
    /**
     * Loading circle, for when server is sleeping.
     */
    private ProgressDialog mLoadingScreen;
    /**
     * Checks for user permission to send SMS
     */
    private boolean isGranted = false;

    /**
     * Checks to see if that username already exists
     */
    private boolean usernameExists = false;

    /**
     * The snackbar for validation
     */
    private Snackbar snackbar;

    /**
     * All users
     */
    private List<User> mUsernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mLoadingScreen = new ProgressDialog(SignupActivity.this);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        confirmPassEditText = (EditText) findViewById(R.id.passwordConfirm);
        snackbar = Snackbar
                .make(usernameEditText, "Please enter a valid username or password", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        ask(findViewById(R.id.register));
        queryCurrUsers();
    }

    /**
     * Switch to the login page.
     * @param v the view
     */
    public void switchToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    /**
     * Event handler attached to sign up button. Handles what
     * will occur once the sign up is clicked. In this case, it will
     * talk to the web server and insert the information.
     *
     * @param v the view
     */
    public void clickedSignUp(View v) {

        final String name, phone, confirmPass;
        final Intent intent = new Intent(this, LoginActivity.class);
        username = usernameEditText.getText().toString(); //grab username
        password = passwordEditText.getText().toString(); //grab password
        name = nameEditText.getText().toString(); // grab name
        phone = phoneEditText.getText().toString(); // grab phone
        confirmPass = confirmPassEditText.getText().toString();
        boolean isValidated = true;

        Log.i("Username", username);
        Log.i("Password", password);
        Log.i("Name", name);
        Log.i("Phone", phone);

        if(username.length() > 0) {
            for (User u : mUsernames) {
                System.out.println(u.getUsername());
                if (u.getUsername().equals(username)) {
                    usernameExists = true;
                }
            }
            if(usernameExists) {
                snackbar.setText("Username is taken");
                snackbar.show();
                isValidated = false;
            }
        }
        if (name.length() == 0) {
            mLoadingScreen.dismiss();
            snackbar.setText("Please enter a valid name");
            snackbar.show();
            isValidated = false;
        }
        if (phone.length() == 0) {
            mLoadingScreen.dismiss();
            snackbar.setText("Please enter a valid phone number");
            snackbar.show();
            isValidated = false;

        }
        if (password.length() > 0 && password.length() < 6) {
            mLoadingScreen.dismiss();
            snackbar.setText("Password must be 6 characters long");
            snackbar.show();
            isValidated = false;
        }
        if (username.length() == 0 || password.length() == 0) {
            mLoadingScreen.dismiss();
            snackbar.setText("Please enter a valid username or password");
            snackbar.show();
            isValidated = false;
        }
        if (!confirmPass.equals(password)) {
            mLoadingScreen.dismiss();
            snackbar.setText("Passwords do not match");
            snackbar.show();
            isValidated = false;
        }

//        if(name.length() > 0 && !usernameExists && phone.length() > 0 && password.length() >= 6 &&
//                username.length() > 0 && confirmPass.equals(password)) {
//            isValidated= true;
//        }
        if(isValidated) {
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
            Call<List<User>> call = api.register(username, password, name, phone);

//        //Make API call, handle success and error
            call.enqueue(new Callback<List<User>>() {

                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    mLoadingScreen.setMessage("Registering...");
                    mLoadingScreen.setIndeterminate(false);
                    mLoadingScreen.setCancelable(true);
                    mLoadingScreen.show();
                    if (response.isSuccessful()) {

                        mLoadingScreen.dismiss();
                        Log.i("SignupActivity", response.toString());
                        if (isGranted) {
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(phone, null, "Welcome to LitChat! Your username is: " + username + ". Select a friend and start chatting!", null, null);

                        } else {
                            snackbar.setText("Permission for SMS denied.");

                        }
                        startActivity(intent); //Switch to new activity
                    }

                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    System.out.println("fail");
                    t.printStackTrace();
                    mLoadingScreen.dismiss();
                    snackbar.setText("Something went wrong. Please retry.");
                    snackbar.show();
                }
            });

        }


    }


    /**
     * Helper method that will allow user to choose if they want
     * an SMS sent to them.
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SignupActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SignupActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SignupActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            isGranted = true;
        }
    }


    public void ask(View v) {
        switch (v.getId()) {
            case R.id.register:
                askForPermission(Manifest.permission.SEND_SMS, MY_PERMISSIONS_REQUEST_SMS);
                break;

            default:
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //sms
                case 1:
                    isGranted = true;

                    break;
            }

//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    private void queryCurrUsers() {
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
        Call<List<User>> call = api.getUsers();

        //Make API call, handle success and error
        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    mUsernames = response.body();
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





