package tcss450.uw.edu.hitmeupv2.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Jason Thai
 * Shema Rezanejad
 * This is the interface we will use to interact with our backend web service
 */

public interface MessagingAPI {
    /** Login
     *
     * @param username the username
     * @param password the password
     * @return user
     */
    @FormUrlEncoded
    @POST("login")
    Call<List<User>> login(@Field("username") String username, @Field("password") String password);

    /**
     * Sign up/ Register user.
     * @param username the username
     * @param password the password
     * @return user
     */
    @FormUrlEncoded
    @POST("register")
    Call<List<User>> register(@Field("username") String username, @Field("password") String password);

    /**
     * Get last conversation from recipient.
     * @param userId users id
     * @return conversation
     */
    @GET("conversations/{userId}")
    Call<List<Conversation>> getConversations(@Path("userId") String userId);

    /**
     * Get all friends
     * @param userId users id
     * @return a user
     */
    @GET("contacts/{userId}")
    Call<List<User>> getContacts(@Path("userId") String userId);
    /**
     * Get all messages
     * @param userId users id is the logged in user id
     * @param otherUserId is the other user's id that is part of the message
     * @return a user
     */
    @GET("messages/{userId}/{otherUserId}")
    Call<List<ChatMessage>> getMessages(@Path("userId") String userId, @Path("otherUserId") String otherUserId);

    /**
     * Sign up/ Register user.
     * @param message the message text
     * @param senderId the sender message id
     * @param recipientId the recipient of the message id
     */
    @FormUrlEncoded
    @POST("send-message/{senderId}/{recipientId}")
    Call<List<User>> sendMessage(@Field("message") String message, @Path("senderId") String senderId, @Path("recipientId") String recipientId);



    // for pictures use @Multipart

}
