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
}
