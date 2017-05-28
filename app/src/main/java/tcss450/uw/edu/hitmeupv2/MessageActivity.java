package tcss450.uw.edu.hitmeupv2;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcss450.uw.edu.hitmeupv2.WebService.ChatMessage;
import tcss450.uw.edu.hitmeupv2.WebService.Conversation;
import tcss450.uw.edu.hitmeupv2.WebService.MessagingAPI;
import tcss450.uw.edu.hitmeupv2.WebService.PostPhoto;

/**
 * Shema Rezanejad
 * Jason Thai
 * <p>
 * Displays the conversation screen, where you can converse with your friend.
 */

public class MessageActivity extends AppCompatActivity {
    /**
     * URL for site
     */
    private static final String BASE_URL = "https://glacial-citadel-99088.herokuapp.com/";
    private int PICK_IMAGE_REQUEST = 1;
    /**
     * Use this if you want to test on a local server with emulator
     */
//    private static final String TEST_URL = "https://glacial-citadel-99088.herokuapp.com/";
    private static final String TEST_URL = "http://10.0.2.2:8888/";

    /**
     * String from edit text that sender is writing.
     */
    private EditText messageET;
    /**
     * Container for all messages.
     */
    private ListView messagesContainer;
    /**
     * Send button.
     */
    private ImageButton sendBtn;
    /**
     * Message adapter .
     */
    private MessageAdapter adapter;
    /**
     * History of conversations.
     */
    private List<ChatMessage> chatHistory;
    /**
     * Stores the current users id (i.e. the sender)
     */
    private String mUserId;
    /**
     * The other user that this user is talking to
     */
    private String otherUserId;
    /**
     * Checking to see if the other user is online.
     */
    private boolean otherUserIsOnline;
    /**
     * Storing the conversation.
     */
    private Conversation mConvo;
    /**
     * Storing toolbar so we can set subtitle
     */
    private Toolbar mActionBar;
    /**
     * The socket
     */
    private Socket mSocket;
    /**
     * This activity.
     */
    private Activity mActivity;
    /**
     * Chatmessage that is added when a chat bubble should be shown
     */
    private ChatMessage mTypingBubble;
    /**
     * Check to see if there is a typing bubble already added
     */
    private boolean hasTypingBubble;
    /**
     * Check to see if user is typing.
     */
    private boolean isTyping;
    /**
     * Button which sends photo
     */
    private ImageButton mPhotoBtn;
    /**
     * Posts and gets from DB
     */
    private PostPhoto mPhoto;

    public MessageActivity() {
        chatHistory = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mActivity = this;
        mUserId = getIntent().getExtras().getString("userId");
        mConvo = (Conversation) getIntent().getSerializableExtra("Conversation");
        mPhoto = new PostPhoto(this, R.layout.activity_message, R.id.sentPhoto);

        // // TODO: 5/27/2017 finish adding picture mail 
        otherUserIsOnline = false;
        hasTypingBubble = false;
        isTyping = false;
        mTypingBubble = new ChatMessage();

        initControls();
        getChatHistory();

        //  must update once communicating with server
        mActionBar = (Toolbar) findViewById(R.id.myMsgToolbar);

        setSupportActionBar(mActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBar.setSubtitle("offline");

        try {
            mSocket = IO.socket(BASE_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        /* Listen for new messages */
        mSocket.on("updateMessageArea", newMessage);
        /* Listen for current sockets connected */
        mSocket.on("usersOnline", usersOnline);
        /* Listen for showChatBubble event */
        mSocket.on("showChatBubbles", chatBubbleEvent);

        JSONObject userIdObj = new JSONObject();

        try {
            userIdObj.put("userId", mUserId);
            userIdObj.put("otherUserId", otherUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("sendUserId", userIdObj);
    }

    private Emitter.Listener newMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        message = data.getString("message");
                        Log.i("This is the message", message);
                        if (hasTypingBubble) {

                            hasTypingBubble = false;
                            isTyping = false;
                            adapter.remove(mTypingBubble);
                            adapter.notifyDataSetChanged();
                            scroll();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessage(message);
                    chatMessage.setDate(getCurrentTime());
                    // add the message to view
                    displayMessage(chatMessage);

                }
            });
        }
    };

    private Emitter.Listener chatBubbleEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            final Timer timer = new Timer();
            final long DELAY = 3000; // milliseconds

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("is typing " + isTyping);
                    if (!hasTypingBubble) {
                        displayTypingBubble();
                    }
                    if (hasTypingBubble && isTyping) {

                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              isTyping = false;
                                                              System.out.println("REMOVE BUBBLE");
                                                              if (hasTypingBubble) {
                                                                  adapter.remove(mTypingBubble);
                                                                  hasTypingBubble = false;

                                                              }
                                                              adapter.notifyDataSetChanged();
                                                              scroll();
                                                              Log.i(MessageActivity.class.getSimpleName(), "User stopped typing");

                                                          }
                                                      }
                                        );

                                    }
                                },
                                DELAY
                        );
                    }


                }
            });

        }
    };

    private Emitter.Listener usersOnline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray onlineUsers = (JSONArray) args[0];
                    Log.d("online users", onlineUsers.toString());

                    // Iterate through onlineUsers
                    for (int i = 0; i < onlineUsers.length(); i++) {
                        try {
                            JSONObject userIdObj = (JSONObject) onlineUsers.get(i);
                            String userId = userIdObj.getString("userId");
                            Log.d("online user", userId);

                            if (userId.equals(otherUserId)) {
                                otherUserIsOnline = true;
                                Log.i("Sockets", "Other user Is online");
                                mActionBar.setSubtitle("online");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

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
        mPhotoBtn = (ImageButton) findViewById(R.id.attachPhoto);


        adapter = new MessageAdapter(MessageActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

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
                chatMessage.setDate(getCurrentTime());
//                chatMessage.setMonthDay();
                //// TODO: 5/26/2017 add monthday to the message
                //// TODO: 5/26/2017 add created at to db

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

        mPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessage chatMessage = new ChatMessage();
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


            }
        });

        messageET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("here");
                JSONObject recipientIdObj = new JSONObject();
                try {
                    recipientIdObj.put("recipientId", otherUserId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                isTyping = true;

                mSocket.emit("isTyping", recipientIdObj);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri u = mPhoto.activityResult(requestCode, resultCode, data);
        final InputStream imageStream;

        ChatMessage photoMsg = new ChatMessage();
        photoMsg.setMe(true);
        photoMsg.setPhotoMsg(true);
        photoMsg.setDate(getCurrentTime());
        photoMsg.setPhotoSrc(mPhoto.getRealPathFromURIPath(u, this));
        displayMessage(photoMsg);


    }


    /**
     * Displaying the message inside the 9 patch image.
     *
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
     * Gets the current time from Simple date format.
     *
     * @return a time
     */
    public String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("K:mm a");

        return timeFormat.format(new Date());

    }

    /**
     * Getter for chat history.
     *
     * @return list of conversation
     */
    private void getChatHistory() {
        final MessageActivity that = this;

        //used to convert JSON to POJO (Plain old java object)
        Gson gson = new GsonBuilder().setLenient().create();

        //Set up retrofit to make our API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //More setup
        MessagingAPI api = retrofit.create(MessagingAPI.class);

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
                    chatHistory = response.body();

                    for (int i = 0; i < chatHistory.size(); i++) {

                        ChatMessage msg = chatHistory.get(i);
                        String recipientUser = msg.getRecipientName();
                        String senderUser = msg.getSenderName();
                        int isPhotoMsg = msg.getIsPhoto();


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat output1 = new SimpleDateFormat("HH:mm a");
                        SimpleDateFormat output2 = new SimpleDateFormat("MMM d ");

//                        System.

                        Date d = null;
                        try {
                            d = sdf.parse(msg.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedTime1 = output1.format(d);
                        String formattedTime2 = output2.format(d);
                        System.out.println("THE MESSAGE " + msg.getMessage());
                        System.out.println("OLD TIME " + msg.getDate());
                        System.out.println("NEW TIME " + formattedTime1);
                        msg.setDate(formattedTime1);
                        msg.setMonthDay(formattedTime2);
                        if (mUserId.equals(msg.getSenderId())) {
                            msg.setMe(true);
                            mActionBar.setTitle(recipientUser);
                        } else {
                            msg.setMe(false);
                            mActionBar.setTitle(senderUser);

                        }

                        if (isPhotoMsg == 1) {
                            msg.setPhotoMsg(true);
                            msg.setPhotoSrc(msg.getMessage());
                            System.out.println("is a photo " + msg.getMessage());

                        }
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


    /**
     * Posts the message sent by user to the server. Also displays
     * the message on the frontend. Use sockets to display in
     * real time.
     *
     * @param message     the message being posted
     * @param senderId    the person who is sending the message
     * @param recipientId the person recieving the message
     */
    public void postMessage(String message, String senderId, String recipientId) {
        Log.i("postMessage", message);
        Log.i("postMessage", senderId);
        Log.i("postMessage", recipientId);


        JSONObject messageObj = new JSONObject();
        try {
            messageObj.put("message", message);
            messageObj.put("userId", senderId);
            messageObj.put("recipientId", recipientId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mSocket.emit("sendMessage", messageObj);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        JSONObject userIdObj = new JSONObject();
        try {
            userIdObj.put("userId", mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("userWentOffline", userIdObj);
        mActionBar.setSubtitle("offline");
        mSocket.disconnect();
    }

    /**
     * Helper method for displaying the typing bubble
     * when the other user is typing.
     */
    private void displayTypingBubble() {
        isTyping = true;
        mTypingBubble.setRecipientTyping(true);
        mTypingBubble.setMessage("");
        mTypingBubble.setDate(null);
        mTypingBubble.setMe(false);
        displayMessage(mTypingBubble);
        hasTypingBubble = true;
    }


}


