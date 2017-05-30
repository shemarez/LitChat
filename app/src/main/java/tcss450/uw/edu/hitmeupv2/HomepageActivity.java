package tcss450.uw.edu.hitmeupv2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.ListItem.RowItem;
import tcss450.uw.edu.hitmeupv2.WebService.Conversation;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;
import tcss450.uw.edu.hitmeupv2.WebService.User;
import tcss450.uw.edu.hitmeupv2.data.ConversationDB;

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
    //private static final String BASE_URL = "http://10.0.2.2:8888/";

    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /** List of friends.*/
    private ArrayList<RowItem> mFriendList = new ArrayList<RowItem>();
    /** Map last convo to the friends id. */
    private HashMap<Integer, Conversation> mFriendMap = new HashMap<Integer, Conversation> ();
    /** Storing user id. */
    private  String mUserId;
    /** Storing path to recipients profile image. */
    private String mProfileImgPath;
    /** File storage. */
    private ConversationDB mConvoDB;
    /** Stores old conversations for sqlite */
    private List<Conversation> mConvos;
    /** For loading purposese */
    private ProgressBar mSpinner;
    /** If there are no current conversations */
    private TextView mNoConvo;
    /** Sets the nav drawer username */
    private TextView mUsername;
    /** Sets the nav drawer profile picture */
    private CircleImageView mProfilePic;
    /** Sets the phone number for nav drawer */
    private TextView mPhoneNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSpinner = (ProgressBar) findViewById(R.id.spinner);
        mNoConvo = (TextView) findViewById(R.id.noConvo);

        setSupportActionBar(toolbar);
        final HomepageActivity  that = this;
        //Call the getConversations method from the interface that we created

        mUserId = getIntent().getExtras().getString("userId");
        String username = getIntent().getExtras().getString("username");

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
//        webserviceHelper();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        mUsername = (TextView)hView.findViewById(R.id.name);
        mProfilePic = (CircleImageView) hView.findViewById(R.id.profile_pic);
        mPhoneNum = (TextView) hView.findViewById(R.id.phone) ;
        getCurrentUserInfo();
        connectionManager(username);


    }

    /**
     * Helper method for creating every item in the list.
     * @param profilePic friends picture
     * @param friendName friends name
     * @param lastConvo last message sent with friend
     */
    private void createRowItems(String profilePic, String friendName, String lastConvo) {

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
            if (id == R.id.nav_friends) {
                Intent friendIntent = new Intent(this, FriendsActivity.class);
                friendIntent.putExtra("userId", mUserId);
                startActivity(friendIntent);

            } else if (id == R.id.nav_profile) {
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("userId", mUserId);
                startActivity(profileIntent);

              } else if(id == R.id.nav_share) {
                Intent intent = new Intent(this, InviteFriends.class);
                intent.putExtra("userId", mUserId);
                startActivity(intent);
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
                intent.putExtra("userId", mUserId);
                intent.putExtra("Conversation", entry.getValue());
            }
        }

        intent.putExtras(extras);
        startActivity(intent);
    }


    /**
     * Queries for conversations
     * @param username the username
     */
    private void webserviceHelper(final String username) {
        final HomepageActivity that = this;
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
        mSpinner.setVisibility( View.VISIBLE);

        System.out.println(mUsername.getText());

        call.enqueue(new Callback<List<Conversation>>() {

            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {

                if (response.isSuccessful()) {
                    List<Conversation> convos = response.body();
                    mConvos = response.body();
                    mConvoDB = new ConversationDB(that);

                    mConvoDB.deleteCourses();

                    mSpinner.setVisibility(View.GONE);


                    if(convos.isEmpty()) {
                        mNoConvo.setVisibility(View.VISIBLE);
                    } else {
                        mNoConvo.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < convos.size(); i++) {
                        Conversation convo = convos.get(i);
                        mFriendMap.put(i, convo);
                        String lastMsg = convos.get(i).getMessage();
                        String senderId = convo.getSenderId();
                        String recipientId = convo.getRecipientId();
                        String senderImg = convo.getSenderProfileImgPath();
                        String recipientImg = convo.getRecipientProfileImgPath();


                        // When creating the friend name label for the row items
                        // we want to display the "opposite name", so if the
                        // logged in user is the sender of the conversation,
                        // display the recipients name for the label, but if
                        // the logged in user is the recipients name, display
                        // the sender name for the label
                        if (mUserId.equals(senderId)) {
                            if(recipientImg != null) {
                                String imgURL = BASE_URL +  "public/" + recipientImg;
                                createRowItems(imgURL, convo.getRecipientName(), lastMsg);
                                mConvoDB.insertConvo(mUserId, senderId, recipientId, lastMsg, convo.getRecipientName(), imgURL);

                            } else {
                                createRowItems(null, convo.getRecipientName(), lastMsg);
                                mConvoDB.insertConvo(mUserId, senderId, recipientId, lastMsg, convo.getRecipientName(), null);
                            }

                        } else {
                            if(senderImg != null) {
                                String imgURL = BASE_URL +  "public/" + senderImg;
                                createRowItems(imgURL, convo.getSenderName(), lastMsg);
                                mConvoDB.insertConvo(mUserId, senderId, recipientId, lastMsg, convo.getSenderName(), imgURL);

                            } else {
                                createRowItems(null, convo.getSenderName(), lastMsg);
                                mConvoDB.insertConvo(mUserId, senderId, recipientId, lastMsg, convo.getSenderName(), null);

                            }

                        }
                    }

                    CustomListViewAdapter adapter = new CustomListViewAdapter(that,R.layout.content_homepage_list,
                            mFriendList, true);

                    adapter.setmTitle(R.id.friendLabel);
                    adapter.setmSubtitleTitle(R.id.lastConvo);
                    adapter.setmImg(R.id.profile_pic);

                    ListView list = (ListView) findViewById(R.id.conversationsList);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(that);


                }
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
                String result = t.getMessage();
                if (result.startsWith("Unable to")) {
                    mSpinner.setVisibility(View.GONE);

                    Toast.makeText(that.getApplicationContext(), result, Toast.LENGTH_LONG)
                            .show();
                    return;


                }

            }
        });
    }


    /**
     * Checks if connected to network, if not use sqlite
     * @param username the users username
     */
    private void connectionManager(String username) {
        ConnectivityManager mgr =(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = mgr.getActiveNetworkInfo();
        if(info != null && info.isConnected()) {
            webserviceHelper(username);
        } else {
            Toast.makeText(this,
                    "No network connection available.",
                    Toast.LENGTH_SHORT) .show();

            if(mConvoDB == null) {
                mConvoDB = new ConversationDB(this);
            }

            if(mConvos == null) {
                mConvos = mConvoDB.getConvos();

                for(int i = 0; i <mConvos.size(); i++) {
                    Conversation c = mConvos.get(i);
                    String lastMsg = c.getMessage();
                    String senderId = c.getSenderId();
                    String recipientId = c.getRecipientId();
                    String senderImg = c.getSenderProfileImgPath();
                    String recipientImg = c.getRecipientProfileImgPath();


                    if(senderId != null ) {
                        createRowItems(senderImg,c.getRecipientName(), lastMsg);

                    } else {
                        createRowItems(recipientImg, c.getSenderName(), lastMsg);
                    }
                }
            }

            CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.content_homepage_list,
                    mFriendList, true);

            adapter.setmTitle(R.id.friendLabel);
            adapter.setmSubtitleTitle(R.id.lastConvo);
            adapter.setmImg(R.id.profile_pic);

            ListView list = (ListView) findViewById(R.id.conversationsList);
            list.setAdapter(adapter);
            list.setOnItemClickListener(this);


        }
    }


    /**
     * loads the users profile pic in the nav drawer
     */
    private void loadProfilePic() {
        if(mProfileImgPath != null) {
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
                    .load(BASE_URL + "public/" + mProfileImgPath)
                    .into(mProfilePic);

        }
    }

    /**
     * Returns the current users information. Used to set
     * info in nav drawer.
     */
    private void getCurrentUserInfo() {
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
        Call<List<User>> call = api.getUserInfo(mUserId);

        //Make API call, handle success and error
        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    User user = response.body().get(0);
                    String userId = user.getUserId();
                    mUsername.setText(user.getUsername());
                    mProfileImgPath = user.getProfileImgPath();
                    mPhoneNum.setText("+" + user.getPhone());
                    loadProfilePic();
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




