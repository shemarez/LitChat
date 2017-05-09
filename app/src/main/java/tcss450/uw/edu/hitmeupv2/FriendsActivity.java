package tcss450.uw.edu.hitmeupv2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendsActivity extends AppCompatActivity {

    private String [] mListArr = {"jason", "shema", "sherry"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_friends_list, R.id.label, mListArr);

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

}
