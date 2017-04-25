package tcss450.uw.edu.hitmeupv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickedSignUp(View v) {
        Intent intent = new Intent(this, SignupActivity.class);

        startActivity(intent);
    }

    public void clickedLogin(View view) {
        Intent intent = new Intent(this, HomepageActivity.class);

        startActivity(intent);
    }
}
