package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import tcss450.uw.edu.hitmeupv2.WebService.User;

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * This activity shows all contacts in the app. You can select one and it will start a new conversation
 * with that particular friend.
 */

public class FriendsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    /** URL of app.*/
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
//    private static final String BASE_URL = "http://10.0.2.2:8888/";

    /** List of friends/contacts.*/
    private ArrayList<RowItem> mFriendList = new ArrayList<RowItem>();
    /** Stores the current users id */
    private  String mUserId;
    /** Stores path of user profile */
    private String mProfileImgPath;
    private HashMap<Integer, User> mFriendMap;
    private ProgressBar mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSpinner = (ProgressBar) findViewById(R.id.spinner);
        mFriendMap = new HashMap<>();
        //Call the getConversations method from the interface that we created
        mUserId = getIntent().getExtras().getString("userId");
        System.out.println("userId: " + mUserId);


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

        webServiceHelper();
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
     * @param profilePic friends picture
     * @param friendName friends name
     * @param lastConvo last message sent with friend
     */
    private void createRowItems(String profilePic, String friendName, String lastConvo) {

        RowItem item = new RowItem(profilePic, friendName, lastConvo);
        mFriendList.add(item);

    }


    /**
     * Helper method. Breaks up web service code.
     */
    private void webServiceHelper() {
        final FriendsActivity that = this;

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        mSpinner.setVisibility(View.VISIBLE);
        Call<List<User>> call = api.getContacts(mUserId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    System.out.println("here");
                    List<User> friends = response.body();
                    for (int i = 0; i < friends.size(); i++) {
                        System.out.println(friends.get(i).getUsername());
                        String friend = friends.get(i).getUsername();
                        String imgPath = friends.get(i).getProfileImgPath();

                        mFriendMap.put(i, friends.get(i));

                        if(imgPath != null) {
                            imgPath = BASE_URL +  "public/" + imgPath;
                            System.out.println(imgPath);
                            createRowItems(imgPath, friend, null);

                        } else {
                            createRowItems(null, friend, null);

                        }
                    }
                    // add new conversation with friend, to the homepage list, only if something was sent
                    // when pressed launch message activity with that friends name
                    CustomListViewAdapter adapter = new CustomListViewAdapter(that,R.layout.activity_friends_list,
                            mFriendList, false);
                    adapter.setmTitle(R.id.label);
                    adapter.setmImg(R.id.friendsProfilePic);
                    adapter.setHasProfilePic(true);
//                    adapter.setmSubtitleTitle(R.id.lastConvo);

                    ListView list = (ListView) findViewById(R.id.friendsList);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(that);
                    mSpinner.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle extras = new Bundle(); // for passing multiple values

        for (Map.Entry<Integer, User> entry : mFriendMap.entrySet()) {
            int key = entry.getKey();


            if (key == position) {
                intent.putExtra("userId", mUserId);
                Conversation newConvo = new Conversation();
                newConvo.setRecipientId(entry.getValue().getUserId());
                newConvo.setSenderId(mUserId);
                newConvo.setUsername(entry.getValue().getUsername());
                intent.putExtra("Conversation", newConvo);
                intent.putExtra("actionBarTitle", entry.getValue().getUsername());

//                entry.getValue().
            }
        }

        intent.putExtras(extras);
        startActivity(intent);
    }


}


