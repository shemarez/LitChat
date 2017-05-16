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

import java.io.IOException;
import java.util.ArrayList;

import tcss450.uw.edu.hitmeupv2.ListItem.RowItem;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private int PICK_IMAGE_REQUEST = 1;
    /** List of friends/contacts.*/
    private ArrayList<RowItem> mFriendList = new ArrayList<RowItem>();
    /** Stores the current users id */
    private  String mUserId;
    /** Stores username */
    private String mUsername = "shemarez";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout t = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        t.setTitle(mUsername);
        setSupportActionBar(toolbar);

        mUserId = getIntent().getExtras().getString("userId");


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

        RowItem first = new RowItem("Shema", "First Name");
        RowItem last = new RowItem("Rezanejad", "Last Name");
        RowItem user = new RowItem("shemarez", "Username");
        RowItem about = new RowItem(null, "About");
        mFriendList.add(first);
        mFriendList.add(last);
        mFriendList.add(user);
        mFriendList.add(about);




//        createRowItems("Shema", "First Name");
//        createRowItems("Rezanejad", "Last Name");
//        createRowItems("shemarez", "Username");
//        createRowItems("About", null);

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
     * Helper method for creating every item in the list.
     * @param friendName friends name
     * @param lastConvo last message sent with friend
     */
    private void createRowItems(String friendName, String lastConvo) {

        RowItem item = new RowItem(friendName, lastConvo);
        mFriendList.add(item);

    }


}
