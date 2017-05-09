package tcss450.uw.edu.hitmeupv2.WebService;

import java.util.Date;

public class Conversation {
    /**
     * messageId is a unique identifier for a message
     */
    private int messageId;
    private String message;
    private Date createdAt;
    private int senderId;
    private int recipientId;
    private String recipientName;

    public int getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }
}
