package tcss450.uw.edu.hitmeupv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * Invites friends by sms to use LitChat
 */
public class InviteFriends extends AppCompatActivity {
    /**
     * Checking the permission.
     */
    private static final int MY_PERMISSIONS_REQUEST_SMS = 1;
    /** The user id */
    private String mUserId;
    /** Users phone number */
    private String mPhone;
    /** If permission is granted to send SMS */
    private boolean isGranted;
    /** For notifying purposes */
    private Snackbar snackbar;
    /** Sets the username */
    private String mUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mUserId = getIntent().getExtras().getString("userId");
        mUsername = getIntent().getExtras().getString("username");
        snackbar = Snackbar
                .make(findViewById(R.id.friendsPhone), "", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Invite Friends");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();

                }
            });
        }
        ask(findViewById(R.id.inviteBtn));



    }


    @Override
    public void onBackPressed() {

        Log.i("pressed back", "back");
        Intent homepage = new Intent(this, HomepageActivity.class);
        homepage.putExtra("userId", mUserId);
        startActivity(homepage);

    }

    /**
     * If user clicked invite, send sms.
     * @param v the view
     */
    public void clickedInvite(View v) {
        if(isGranted) {
            EditText phone = (EditText) findViewById(R.id.friendsPhone);
            if (phone.getText().toString().length() > 0 ) {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phone.getText().toString(), null, "Start chatting with me on LitChat!", null, null);
                snackbar.setText("SMS was sent");
                snackbar.show();
            } else {
                snackbar.setText("Invalid phone number");
                snackbar.show();
            }


        } else {
            snackbar.setText("Permission to send SMS was denied.");
            snackbar.show();
        }

    }


    /**
     * Helper method that will allow user to choose if they want
     * an SMS sent to them.
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(InviteFriends.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(InviteFriends.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(InviteFriends.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(InviteFriends.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            isGranted = true;
        }
    }


    /**
     * Shows permission dialog
     * @param v the view
     */
    public void ask(View v) {
        switch (v.getId()) {
            case R.id.inviteBtn:
                askForPermission(Manifest.permission.SEND_SMS, MY_PERMISSIONS_REQUEST_SMS);
                break;

            default:
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //sms
                case 1:
                    isGranted = true;

                    break;
            }

//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

}
