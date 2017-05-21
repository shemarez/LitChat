package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.ListItem.RowItem;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;
import tcss450.uw.edu.hitmeupv2.WebService.User;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
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

    public ProfileActivity() {
        mProfileImgPath = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout t = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);



        mUserId = getIntent().getExtras().getString("userId");

        // TODO: SEND USER ID WHEN PRESSING THE BACK BUTTON TO HOMEPAGE ACTIVITY
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();

                }
            });
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        retrieveProfileImg(t);
        generateProfile();


        t.setTitle(mUsername);
        setSupportActionBar(toolbar);




    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.expandedProfilePic);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onBackPressed() {

        Log.i("pressed back", "back");
        Intent homepage = new Intent(this, HomepageActivity.class);
        homepage.putExtra("userId", mUserId);
        startActivity(homepage);

    }

    /**
     * Private helper which handles the GET of the profile image path.
     * @param toolbar
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

                    String imgURL = BASE_URL +  "public/" + mProfileImgPath;

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
        createRowItems("Shema", "First Name");
        createRowItems("Rezanejad", "Last Name");
        createRowItems("shemarez", "Username");
        createRowItems(null , "About");
        createRowItems(null , "Logout");

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


}
