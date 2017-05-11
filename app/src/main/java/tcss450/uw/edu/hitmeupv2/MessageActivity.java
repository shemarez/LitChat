package tcss450.uw.edu.hitmeupv2;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * Displays the conversation screen, where you can converse with your friend.
 */

public class MessageActivity extends AppCompatActivity  {
    /** String from edit text that sender is writing. */
    private EditText messageET;
    /** Container for all messages. */
    private ListView messagesContainer;
    /** Send button. */
    private ImageButton sendBtn;
    /** Message adapter . */
    private MessageAdapter adapter;
    /** History of conversations. */
    private ArrayList<ChatMessage> chatHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initControls();
        // TODO: must update once communicating with server
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Loading messages, and handling what happens once the send button
     * has been clicked.
     */
    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);

//        TextView meLabel = (TextView) findViewById(R.id.meLbl);
//        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
//        companionLabel.setText("My Buddy");

        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(getCurrentTime());
                chatMessage.setMe(true);

                messageET.setText("");

                displayMessage(chatMessage);
            }
        });


    }

    /**
     * Displaying the message inside the 9 patch image.
     * @param message the string being sent
     */
    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    /**
     * Allows you to scroll through the list.
     */
    private void scroll() {

        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    /**
     * Loading chat history. Right now
     * this is just loading dummy data.
     */
    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
//        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        msg.setDate(getCurrentTime());
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
//        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        msg1.setDate(getCurrentTime());
        chatHistory.add(msg1);

        adapter = new MessageAdapter(MessageActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }


    /**
     * Gets the current time from Simple date format.
     * @return a time
     */
    public String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("K:mm a");

        return timeFormat.format(new Date());

    }

    /**
     * Getter for chat history.
     * @return list of conversation
     */
    public ArrayList<ChatMessage> getChatHistory() {
        return chatHistory;
    }



}


