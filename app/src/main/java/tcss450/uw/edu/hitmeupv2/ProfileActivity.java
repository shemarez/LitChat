package tcss450.uw.edu.hitmeupv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import tcss450.uw.edu.hitmeupv2.ListItem.RowItem;
import tcss450.uw.edu.hitmeupv2.WebService.PostPhoto;
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
    /** My phone number */
    private String mPhone;
    /** My name */
    private String mName;
    /** Post to db */
    private PostPhoto mPhoto;
    /** For loading purposes */
    private ProgressBar mSpinner;


    /**
     * Sets the img path to null
     */
    public ProfileActivity() {
        mProfileImgPath = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        CollapsingToolbarLayout t = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        mSpinner = (ProgressBar) findViewById(R.id.spinner);

        mUserId = getIntent().getExtras().getString("userId");

        mPhoto = new PostPhoto(this, R.layout.activity_profile, R.id.expandedProfilePic);

        mSpinner.setVisibility(View.GONE);



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

        final ProfileActivity that = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoto.checkPermissions(that);
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
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.clear();
            edit.commit();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();

        }
        if(position == 3) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);

            d.setTitle("About");
            // set dialog message
                     d
                    .setMessage("LitChat is a messaging app that easily allows you to message friends.")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            ProfileActivity.this.finish();
                        }
                    });


            // create alert dialog
            AlertDialog alertDialog = d.create();

            // show it
            alertDialog.show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri u = mPhoto.activityResult(requestCode, resultCode, data);

        if(u != null) {
            final InputStream imageStream;
            try {
                mSpinner.setVisibility(View.GONE);

                imageStream = getContentResolver().openInputStream(u);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ((ImageView)findViewById(R.id.expandedProfilePic)).setImageBitmap(selectedImage);
                mPhoto.postProfilePic(mPhoto.getRealPathFromURIPath(u, this), mUserId);
            } catch (FileNotFoundException e) {
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
     * Call this in post photo so that the rows are created.
     * @param toolbar Collpasing toolbar
     */
    public void doSomething(CollapsingToolbarLayout toolbar) {
        User me = mPhoto.getUser();
        mProfileImgPath = mPhoto.getmProfileImgPath();
        mUsername = mPhoto.getmUsername();
        mPhone = mPhoto.getmPhone();
        mName = mPhoto.getmName();

        createRowItems(mName, "Name");
        createRowItems(mUsername, "Username");
        createRowItems(mPhone, "Phone Number");
        createRowItems(null , "About");
        createRowItems(null , "Logout");
        generateProfile();

        mSpinner.setVisibility(View.GONE);


        String imgURL = BASE_URL +  "public/" + mProfileImgPath;

        System.out.println("img url " +  imgURL);
        if(mProfileImgPath != null) {
            ImageView profilePic = (ImageView) findViewById(R.id.expandedProfilePic);
            Picasso.Builder builder = new Picasso.Builder(this);
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




    /**
     * Private helper which handles the GET of the profile image path.
     * @param toolbar the action bar for setting the photo
     */
    private void retrieveProfileImg(final CollapsingToolbarLayout toolbar) {
        final ProfileActivity that = this;

        mSpinner.setVisibility(View.VISIBLE);

       boolean isSuccess=  mPhoto.retrieveProfileImg(toolbar, mUserId);

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

        mPhoto.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }








}
