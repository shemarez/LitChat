package tcss450.uw.edu.hitmeupv2;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.WebService.ChatMessage;
import tcss450.uw.edu.hitmeupv2.WebService.Conversation;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;
import tcss450.uw.edu.hitmeupv2.WebService.User;

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * Displays the conversation screen, where you can converse with your friend.
 */

public class MessageActivity extends AppCompatActivity  {
    /**
     * Use this if you want to test on a local server with emulator
     */
    private static final String TEST_URL = "http://10.0.2.2:8888/";
    /** String from edit text that sender is writing. */
    private EditText messageET;
    /** Container for all messages. */
    private ListView messagesContainer;
    /** Send button. */
    private ImageButton sendBtn;
    /** Message adapter . */
    private MessageAdapter adapter;
    /** History of conversations. */
    private List<ChatMessage> chatHistory;
    /** Stores the current users id */
    private String mUserId;
    private Conversation mConvo;

    public MessageActivity() {
        chatHistory = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mUserId = getIntent().getExtras().getString("userId");
        mConvo = (Conversation) getIntent().getSerializableExtra("Conversation");

        System.out.println(mUserId);
        System.out.println(mConvo);

        initControls();
        getChatHistory();
        //  must update once communicating with server
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

        adapter = new MessageAdapter(MessageActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        // loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageText);
                chatMessage.setMe(true);

                messageET.setText("");

                displayMessage(chatMessage);

                String otherUserId;
                if (mUserId.equals(mConvo.getSenderId())) {
                    otherUserId = mConvo.getRecipientId();
                } else {
                    otherUserId = mConvo.getSenderId();
                }
                postMessage(messageText, mUserId, otherUserId);
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
        msg.setId("1");
        msg.setMe(false);
        msg.setMessage("Welcome to LitChat");
//        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        msg.setDate(getCurrentTime());
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId("2");
        msg1.setMe(false);
        msg1.setMessage("As of right now, frontend messaging does not work. However " +
                "the frontend display for messaging works! Please try typing and " +
                "sending a message! Dont forget to use some emojis!");
//        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        msg1.setDate(getCurrentTime());
        chatHistory.add(msg1);

        for (int i=0; i < chatHistory.size(); i++) {
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
    private void getChatHistory() {
        final MessageActivity that = this;



        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TEST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        String otherUserId;
        if (mUserId.equals(mConvo.getSenderId())) {
            otherUserId = mConvo.getRecipientId();
        } else {
            otherUserId = mConvo.getSenderId();
        }

        System.out.println("mUserId: " + mUserId);
        System.out.println("otherUserId: " + otherUserId);

        Call<List<ChatMessage>> call = api.getMessages(mUserId, otherUserId);
        call.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    System.out.println("here");
                    chatHistory = response.body();

                    for (int i = 0; i < chatHistory.size(); i++) {

                        ChatMessage msg = chatHistory.get(i);
                        System.out.println("ChatMessage:");
                        System.out.println(msg.getSenderId());

                        if (mUserId.equals(msg.getSenderId())) {
                            msg.setMe(true);
                        } else {
                            msg.setMe(false);
                        }

                        String message = msg.getMessage();
                        // TODO: 5/16/2017  fix datetime format
                        displayMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                System.out.println("fail");
                t.printStackTrace();
            }
        });
    }


    public void postMessage(String message, String senderId, String recipientId) {
        Log.i("postMessage", message);
        Log.i("postMessage", senderId);
        Log.i("postMessage", recipientId);

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TEST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

        Call<List<User>> call = api.sendMessage(message, mUserId, recipientId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    System.out.println("here");

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


