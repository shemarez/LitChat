package tcss450.uw.edu.hitmeupv2.WebService;

/**
 * Created by Shema on 4/20/2017.
 */

public class ChatMessage {
    /** The message id. */
    private String messageId;
    /** Checking if the user is the sender. */
    private boolean isMe; // Database doesn't set this, we set it ourselves in MessageActivity
    /** Storing the message. */
    private String message;
    /** Storing the user id. */
    private String senderName;
    private String recipientName;
    private String senderId;
    /** Storing the recipient id. */
    private String recipientId;
    /** Storing the date and time of message. */
    private String createdAt;

    /**
     * Getter.
     * @return message id
     */
    public String getId() {
        return messageId;
    }

    /**
     * Setter.
     * @param id message id
     */
    public void setId(String id) {
        this.messageId = id;
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
    public String getSenderId() {
        return senderId;
    }

    /** Setter
     *
     * @param senderId set the new user id
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    /** Date
     *
     * @return  a date
     */
    public String getDate() {
        return createdAt;
    }
    /** Setter
     *
     * @param dateTime set the new date
     */
    public void setDate(String dateTime) {
        this.createdAt = dateTime;
    }

    /**
     * Setter
     * @return the recipient id
     */
    public String getRecipientId() {
        return recipientId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientName() {
        return  recipientName;
    }
}
