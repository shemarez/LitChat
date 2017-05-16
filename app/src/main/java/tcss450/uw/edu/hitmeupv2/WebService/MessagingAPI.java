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
     * @param senderId users id
     * @return a user
     */
    @GET("messages/{senderId}/{recieverId}")
    Call<List<ChatMessage>> getMessages(@Path("senderId") int senderId, @Path("recieverId") int recieverId);

    /**
     * Sign up/ Register user.
     * @param username the username
     * @param password the password
     * @return user
     */
    @FormUrlEncoded
    @POST("send-message/{senderId}/{reciverId}")
    Call<List<User>> sendMessages(@Field("username") String username, @Field("password") String password);



    // for pictures use @Multipart

}
