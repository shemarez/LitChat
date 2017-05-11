package tcss450.uw.edu.hitmeupv2;

/**
 * Created by Shema on 4/20/2017.
 */

public class ChatMessage {
    /** The message id. */
    private long id;
    /** Checking if the user is the sender. */
    private boolean isMe;
    /** Storing the message. */
    private String message;
    /** Storing the user id. */
    private Long userId;
    /** Storing the date and time of message. */
    private String dateTime;

    /**
     * Getter.
     * @return message id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter.
     * @param id message id
     */
    public void setId(long id) {
        this.id = id;
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
        return userId;
    }

    /** Setter
     *
     * @param userId set the new user id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    /** Date
     *
     * @return  a date
     */
    public String getDate() {
        return dateTime;
    }
    /** Setter
     *
     * @param dateTime set the new date
     */
    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }
}
