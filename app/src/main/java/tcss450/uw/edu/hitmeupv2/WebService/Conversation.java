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
    private int message_id;
    /** The message sent */
    private String message;
    /** Date message was sent */
    private Date created_at;
    /** The user that is sending, id */
    private int sender_id;
    /** Recipient id. */
    private String recipient_id;
    /** Recipient name. */
    private String recipientName;

    /**
     * Getter.
     * @return message id
     */
    public int getMessageId() {
        return message_id;
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
    public Date getCreated_at() {
        return created_at;
    }

    /**
     * Getter.
     * @return sender id
     */
    public int getSenderId() {
        return sender_id;
    }

    /**
     * Getter.
     * @return recipient id
     */
    public String getRecipientId() {
        return recipient_id;
    }

    /**
     * Getter.
     * @return recipient name
     */
    public String getRecipientName() {
        return recipientName;
    }
}
