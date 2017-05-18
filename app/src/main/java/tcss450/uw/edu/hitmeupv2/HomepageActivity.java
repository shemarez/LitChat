package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.ListItem.RowItem;
import tcss450.uw.edu.hitmeupv2.WebService.Conversation;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * This activity is the main "hub" for the entire app. It
 * has a nav drawer, where you can access the "contacts" page.
 * Also queries for a list of friends you are currently conversing with.
 * Generated by android studio.shem
 */
public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener {
    /** URL for site */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /** List of friends.*/
    private ArrayList<RowItem> mFriendList = new ArrayList<RowItem>();
    /** Map last convo to the friends id. */
    private HashMap<Integer, Conversation> mFriendMap = new HashMap<Integer, Conversation> ();
    /** Storing user id. */
    private  String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final HomepageActivity  that = this;
        //Call the getConversations method from the interface that we created
        mUserId = getIntent().getExtras().getString("userId");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(that, FriendsActivity.class);
                intent.putExtra("userId", mUserId);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        Call<List<Conversation>> call = api.getConversations(mUserId);

        call.enqueue(new Callback<List<Conversation>>() {

            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    List<Conversation> convos = response.body();
                    for (int i = 0; i < convos.size(); i++) {
                        Conversation convo = convos.get(i);
                        mFriendMap.put(i, convo);
                        String lastMsg = convos.get(i).getMessage();
                        String senderId = convo.getSenderId();

                        // When creating the friend name label for the row items
                        // we want to display the "opposite name", so if the
                        // logged in user is the sender of the conversation,
                        // display the recipients name for the label, but if
                        // the logged in user is the recipients name, display
                        // the sender name for the label
                        if (mUserId.equals(senderId)) {
                            createRowItems(0, convo.getRecipientName(), lastMsg);

                        } else {
                            createRowItems(0, convo.getSenderName(), lastMsg);
                        }
                    }

                    CustomListViewAdapter adapter = new CustomListViewAdapter(that,R.layout.content_homepage_list,
                            mFriendList, true);

                    adapter.setmTitle(R.id.friendLabel);
                    adapter.setmSubtitleTitle(R.id.lastConvo);

                    ListView list = (ListView) findViewById(R.id.conversationsList);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(that);

                }
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });
    }

    /**
     * Helper method for creating every item in the list.
     * @param profilePic friends picture
     * @param friendName friends name
     * @param lastConvo last message sent with friend
     */
    private void createRowItems(int profilePic, String friendName, String lastConvo) {

        RowItem item = new RowItem(profilePic, friendName, lastConvo);
        mFriendList.add(item);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else
            if (id == R.id.nav_friends) {
                Intent friendIntent = new Intent(this, FriendsActivity.class);
                friendIntent.putExtra("userId", mUserId);
                startActivity(friendIntent);

            } else if (id == R.id.nav_profile) {
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("userId", mUserId);
                startActivity(profileIntent);

              }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle extras = new Bundle(); // for passing multiple values

        for (Map.Entry<Integer, Conversation> entry : mFriendMap.entrySet()) {
            int key = entry.getKey();

            String username = entry.getValue().getRecipientName();
            if (key == position) {
                System.out.println(key);
                System.out.println(entry.getValue());
                intent.putExtra("userId", mUserId);
                intent.putExtra("Conversation", entry.getValue());
            }
        }

        intent.putExtras(extras);
        startActivity(intent);
    }
}
