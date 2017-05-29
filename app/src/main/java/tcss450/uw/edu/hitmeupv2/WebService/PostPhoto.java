package tcss450.uw.edu.hitmeupv2.WebService;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.ProfileActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shema on 5/26/2017.
 */

public class PostPhoto {

    /**
     * Checking the permission.
     */
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    /**    */
    private static int PICK_IMAGE_REQUEST = 1;
    /** Use this if you want to test on a local server with emulator. */
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /** URL for site */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    private Context mContext;
    private ImageView mImageView;
    private User mUser;
    /** Stores path to profile image. */
    private String mProfileImgPath;
    private String mPhone;
    private String mName;
    private String mUsername;
    private boolean retrievedImg;

    public PostPhoto(Context context, int layout, int imageViewId) {
        mContext = context;
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        mImageView = (ImageView) view.findViewById(imageViewId);

    }

    public Uri activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                android.net.Uri uri = data.getData();
//                final InputStream imageStream = mContext.getContentResolver().openInputStream(uri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                mImageView.setImageBitmap(selectedImage);
                return uri;

        }

        return null;
    }



    /**
     * Getting the real path of the image selected.
     * @param contentURI the uri of the img
     * @param activity this
     * @return the path
     */
    public String getRealPathFromURIPath(Uri contentURI, Activity activity) {
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
    public void postProfilePic(String path, String userId) {
        File file = new File(path);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part avatar = MultipartBody.Part.createFormData("photo", file.getName(), mFile);

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
        Call<List<User>> call = api.postProfilePic(avatar, userId);

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
     * Post the photo to the server.
     * @param path the file path name
     */
    public void sendPhoto(String path, String senderId, String recipientId) {
        File file = new File(path);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), mFile);

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
        Call<List<User>> call = api.sendPhoto(photo, senderId, recipientId);

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
    public boolean retrieveProfileImg(final CollapsingToolbarLayout toolbar, String userId) {
        final Context that = mContext;

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TEST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        Call<List<User>> call = api.getUserInfo(userId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    System.out.println("here");
                    retrievedImg = true;
                    User me = response.body().get(0);


                    setUser(me);
                    setmName(me.getName());
                    setmPhone(me.getPhone());
                    setmProfileImgPath(me.getProfileImgPath());
                    setmUsername(me.getUsername());

                    if(mContext instanceof  ProfileActivity) {
                        ((ProfileActivity)mContext).doSomething(toolbar);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });

        return retrievedImg;
    }

    /**
     * Helper method that will allow user to choose if they want
     * their images to be read.
     */
    public void checkPermissions(Activity act) {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(act,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(act,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(act,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

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

    public void setUser(User theUser) {
        mUser = theUser;
    }

    public User getUser() {
        return mUser;
    }

    public String getmProfileImgPath() {
        return mProfileImgPath;
    }

    public void setmProfileImgPath(String mProfileImgPath) {
        this.mProfileImgPath = mProfileImgPath;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }
}
