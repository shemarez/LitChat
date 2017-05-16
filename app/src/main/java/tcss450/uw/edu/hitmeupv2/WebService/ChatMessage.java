package tcss450.uw.edu.hitmeupv2.WebService;

/**
 * Created by Shema on 4/20/2017.
 */

public class ChatMessage {
    /** The message id. */
    private int message_id;
    /** Checking if the user is the sender. */
    private boolean isMe;
    /** Storing the message. */
    private String message;
    /** Storing the user id. */
    private int user_id;
    /** Storing the recipient id. */
    private int recipient_id;
    /** Storing the date and time of message. */
    private String created_at;

    /**
     * Getter.
     * @return message id
     */
    public int getId() {
        return message_id;
    }

    /**
     * Setter.
     * @param id message id
     */
    public void setId(int id) {
        this.message_id = id;
    }

    /**
     * Getter.
     * @return boolean
     */
    public boolean getIsme() {
        return isMe;
    }

    /**
     * Setter.
     * @param isMe boolean
     */
    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    /**
     * Getter
     * @return get message
     */
    public String getMessage() {
        return message;
    }

    /** Setter
     *
     * @param message set the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter.
     * @return get user id
     */
    public long getUserId() {
        return user_id;
    }

    /** Setter
     *
     * @param userId set the new user id
     */
    public void setUserId(int userId) {
        this.user_id = userId;
    }
    /** Date
     *
     * @return  a date
     */
    public String getDate() {
        return created_at;
    }
    /** Setter
     *
     * @param dateTime set the new date
     */
    public void setDate(String dateTime) {
        this.created_at = dateTime;
    }

    /**
     * Setter
     * @return the recipient id
     */
    public int getRecieverId() {
        return recipient_id;
    }

    /**
     * Setter
     * @param recieverId the id
     */
    public void setRecieverId(int recieverId) {
        this.recipient_id = recieverId;
    }
}
