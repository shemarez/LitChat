package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    /** List of friends/contacts.*/
    private ArrayList<RowItem> mFriendList = new ArrayList<RowItem>();
    /** Stores the current users id */
    private  String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    private void createRowItems(int profilePic, String friendName, String lastConvo) {

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
//                        String lastMsg = convos.get(i).getMessage();
                        createRowItems(0, friend, null);
                    }
                    // add new conversation with friend, to the homepage list, only if something was sent
                    // when pressed launch message activity with that friends name
                    CustomListViewAdapter adapter = new CustomListViewAdapter(that,R.layout.activity_friends_list,
                            mFriendList);

                    ListView list = (ListView) findViewById(R.id.friendsList);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(that);


                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
}
