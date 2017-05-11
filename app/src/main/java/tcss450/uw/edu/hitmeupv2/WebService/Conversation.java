package tcss450.uw.edu.hitmeupv2.WebService;

import java.util.Date;

/**
 * Shema Rezanejad
 * Jason Thai
 *
 * Used to read conversation information from the web service.
 */
public class Conversation {
    /**
     * messageId is a unique identifier for a message
     */
    private int messageId;
    /** The message sent */
    private String message;
    /** Date message was sent */
    private Date createdAt;
    /** The user that is sending, id */
    private int senderId;
    /** Recipient id. */
    private int recipientId;
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
    public int getSenderId() {
        return senderId;
    }

    /**
     * Getter.
     * @return recipient id
     */
    public int getRecipientId() {
        return recipientId;
    }

    /**
     * Getter.
     * @return recipient name
     */
    public String getRecipientName() {
        return recipientName;
    }
}
