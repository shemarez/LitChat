package tcss450.uw.edu.hitmeupv2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.ListItem.RowItem;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;
import tcss450.uw.edu.hitmeupv2.WebService.User;

/**
 * This class generates the profile activity.
 * Upload a new profile picture, view information, and logout.
 */

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    /** Checking the permission. */
    private static  final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    /**    */
    private int PICK_IMAGE_REQUEST = 1;
    /** Use this if you want to test on a local server with emulator. */
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /** URL for site */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    /** List of friends/contacts.*/
    private ArrayList<RowItem> mFriendList = new ArrayList<RowItem>();
    /** Stores the current users id */
    private  String mUserId;
    /** Stores username */
    private String mUsername;
    /** Stores path to profile image. */
    private String mProfileImgPath;
    private String mFilePath;
    private String mPhone;
    private String mName;

    public ProfileActivity() {
        mProfileImgPath = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        CollapsingToolbarLayout t = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);


        mUserId = getIntent().getExtras().getString("userId");


        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("PRESSED BACK", mUserId);

                    onBackPressed();

                }
            });
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        retrieveProfileImg(t);
        t.setTitle(mUsername);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 4) {
            System.out.println("LOGOUT");

            //TODO IMPLEMENT LOGOUT
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                android.net.Uri uri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageView = (ImageView) findViewById(R.id.expandedProfilePic);
                imageView.setImageBitmap(selectedImage);

                postProfilePic(getRealPathFromURIPath(uri, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {

        Intent homepage = new Intent(this, HomepageActivity.class);
        homepage.putExtra("userId", mUserId);
        startActivity(homepage);

    }

    /**
     * Getting the real path of the image selected.
     * @param contentURI the uri of the img
     * @param activity this
     * @return the path
     */
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    /**
     * Post the photo to the server.
     * @param path the file path name
     */
    private void postProfilePic(String path) {
//        String path = "file//:" + thePath;
//        File file = null;

        // Get the file instance
        File file = new File(path);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), mFile);

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
        Call<List<User>> call = api.postProfilePic(avatar, mUserId);

//        //Make API call, handle success and error
        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                System.out.println("IN ON RESPONSE");
                if (response.isSuccessful()) {

                    Log.i("ProfileActivity", response.toString());
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });


    }

    /**
     * Private helper which handles the GET of the profile image path.
     * @param toolbar the action bar for setting the photo
     */
    private void retrieveProfileImg(final CollapsingToolbarLayout toolbar) {
        final ProfileActivity that = this;

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
                    mPhone = me.getPhone();
                    mName = me.getName();

                    createRowItems(mUsername, "Username");
                    createRowItems(mName, "Name");
                    createRowItems(mPhone, "Phone Number");
                    createRowItems(null , "About");
                    createRowItems(null , "Logout");
                    generateProfile();


                    String imgURL = TEST_URL +  "public/" + mProfileImgPath;

                    System.out.println("img url " +  imgURL);
                    if(mProfileImgPath != null) {
                        ImageView profilePic = (ImageView) findViewById(R.id.expandedProfilePic);
                        Picasso.Builder builder = new Picasso.Builder(that);
                        builder.listener(new Picasso.Listener()
                        {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                            {
                                exception.printStackTrace();
                            }
                        });
                        builder.build()
                                .load(imgURL)
                                .into(profilePic);
                    }

                    if(mUsername != null) {
                        toolbar.setTitle(mUsername);
                    } else {
                        toolbar.setTitle("LitChat");
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



    /**
     * Helper method for creating every item in the list.
     * @param friendName friends name
     * @param lastConvo last message sent with friend
     */
    private void createRowItems(String friendName, String lastConvo) {

        RowItem item = new RowItem(friendName, lastConvo);
        mFriendList.add(item);

    }

    /**
     * Generates list of items in profile page.
     */
    private void generateProfile() {

        for (int i = 0; i < mFriendList.size(); i++) {
            System.out.println(mFriendList.get(i).getmTitle());
        }

        CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.content_profile_list,
                mFriendList, true);

        adapter.setmTitle(R.id.settingdesc);
        adapter.setmSubtitleTitle(R.id.theSetting);

        ListView list = (ListView) findViewById(R.id.profileList);
        ViewCompat.setNestedScrollingEnabled(list,true);

        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /**
     * Helper method that will allow user to choose if they want
     * their images to be read.
     */
    private void checkPermissions() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }





}
