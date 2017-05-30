package tcss450.uw.edu.hitmeupv2.WebService;

import java.io.Serializable;
import java.util.Date;

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * Used to read conversation information from the web service.
 */
public class Conversation implements Serializable {
    /**
     * messageId is a unique identifier for a message
     */
    private int messageId;
    /** The message sent */
    private String message;
    /** Date message was sent */
    private Date createdAt;
    /** The user that is sending, id */
    private String senderId;
    /** Recipient id. */
    private String recipientId;
    /** Sender name */
    private String senderName;
    /** Recipient name. */
    private String recipientName;
    /** Profile Image Path name, for getting profile pic.
     * Only for the sender of the message. */
    private String senderProfileImgPath;
    /** Profile Image Path name, for getting profile pic.
     * Only for the sender of the message. */
    private String recipientProfileImgPath;


    /**
     * Getter.
     * @return message id
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Getter.
     * @return message string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter.
     * @return date created at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter.
     * @return sender id
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Getter.
     * @return recipient id
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Getter for the sender name (current user in app)
     * @return senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Getter.
     * @return recipient name
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * Return the path of the image..
     * @return  profile image path
     */
    public String getSenderProfileImgPath() {
        return senderProfileImgPath;
    }
    /**
     * Return the path of the image..
     * @return  profile image path
     */
    public String getRecipientProfileImgPath() {
        return recipientProfileImgPath;
    }

    /**
     * Sets the recipient id
     * @param id string
     */
    public void setRecipientId(String id) {
        this.recipientId = id;
    }

    /**
     * Sets the sender of the message id
     * @param id string
     */
    public void setSenderId(String id) {
        this.senderId = id;
    }

    /**
     * Sets the username of the recipient
     * @param username string
     */
    public void setUsername(String username) {
        recipientName = username;
    }

    /**
     * Sets the message of the conversation
     * @param message last message
     */

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the path of the senders profile pic
     * @param path the url
     */

    public void setSenderProfileImgPath(String path) {
        this.senderProfileImgPath = path;
    }

    /**
     * Sets the path of the recipient profile pic
     * @param path the url
     */
    public void setRecipientProfileImgPath(String path) {
        this.recipientProfileImgPath = path;
    }

}
