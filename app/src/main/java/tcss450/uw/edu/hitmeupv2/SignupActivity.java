package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void clickedLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }
}
