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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.Chat.ChatContent;
import tcss450.uw.edu.hitmeupv2.WebService.Conversation;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ChatFragment.OnListFragmentInteractionListener {

    private static final String LIST1_TAB_TAG = "List1";
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";

    private String [] mListArr = {"jason", "shema", "sherry"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final HomepageActivity  that = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(that, FriendsActivity.class);
                startActivity(intent);
//
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.content_homepage_list, R.id.friendLabel, mListArr);

        ListView list = (ListView) findViewById(R.id.conversationsList);
        list.setAdapter(adapter);

//        if(findViewById(R.id.fragment_container) != null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, new ChatFragment()).commit();
//
//        }

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        //Call the getConversations method from the interface that we created
        String userId = getIntent().getExtras().getString("userId");
        System.out.println("userId: " + userId);
        Call<List<Conversation>> call = api.getConversations(userId);

        call.enqueue(new Callback<List<Conversation>>() {

            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    System.out.println("here");
                    List<Conversation> convos = response.body();
                    for (int i = 0; i < convos.size(); i++) {
                        System.out.println(convos.get(i).getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });


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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(ChatContent.ChatItem item) {

    }
}
