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

import tcss450.uw.edu.hitmeupv2.Chat.ChatContent;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChatFragment.OnListFragmentInteractionListener {
    private static final String LIST1_TAB_TAG = "List1";


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
                Intent intent = new Intent(that, MessageActivity.class);
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

        if(findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ChatFragment()).commit();

        }


       // createTabs();

    }

//    public void createTabs() {
//        final TabHost  host = (TabHost) findViewById(R.id.tabHost);
//        host.setup();
//
//        // Tab 1: Chats
//        TabHost.TabSpec spec = host.newTabSpec("Chats");
//        spec.setContent(R.id.chatsTab);
//        spec.setIndicator("Chats");
////        final ListView listView1 = (ListView) findViewById(R.id.list1);
//        host.addTab(spec);
////        host.addTab(host.newTabSpec(LIST1_TAB_TAG).setIndicator(LIST1_TAB_TAG).setContent(new TabHost.TabContentFactory() {
////            @Override
////            public View createTabContent(String tag) {
////                return listView1;
////            }
////        }));
//        // Tab 2: Group Chats
//        spec = host.newTabSpec("GroupChats");
//        spec.setContent(R.id.GroupChats);
//        spec.setIndicator("GroupChats");
//        host.addTab(spec);
//
//        // Tab 3: Friends
//        spec = host.newTabSpec("Friends");
//        spec.setContent(R.id.Friends);
//        spec.setIndicator("Friends");
//        host.addTab(spec);
//
//        for(int i=0; i < host.getTabWidget().getChildCount(); i++) {
//            host.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ff6600"));
//            host.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#333333"));
//        }
//
//        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
////                int tab = host.getCurrentTab();
////
////                System.out.println(tabId);
////                System.out.println("Tab: " + tab);
////                host.getTabWidget().getChildAt(tab).setBackgroundColor(Color.parseColor("#ff6600"));
//
//                for(int i=0;i<host.getTabWidget().getChildCount();i++)
//                {
//                    host.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#333333")); //unselected
//                }
//                host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundColor(Color.parseColor("#ff6600")); // selected
//
//            }
//
//
//        });
//
//
//    }

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
