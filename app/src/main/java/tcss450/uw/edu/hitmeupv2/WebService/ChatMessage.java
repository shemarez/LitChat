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
    /** Name of the recipient. */
    private String recipientName;
    /** Name of the sender. */
    private String senderId;
    /** Storing the recipient id. */
    private String recipientId;
    /** Storing the date and time of message. */
    private String createdAt;
    /** Checks to see if recipient is typing. */
    private boolean isRecipientTyping;
    /** Storing the month and day of text*/
    private String monthDay;
    /** Check to see if message is photo message */
    private boolean isPhotoMsg;
    /** Sets the imageview src for photo message */
    private String photoSrc;
    /** Checks to see if it is a photo from DB */
    private int isPhoto;

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
     * Set the month and day of the message.
     *
     * @param theMonthDay ex Apr 21
     */
    public void setMonthDay(String theMonthDay) {
        this.monthDay = theMonthDay;
    }


    /**
     * Setter
     * @return the recipient id
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     *
     * @return
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     *
     * @return
     */
    public String getRecipientName() {
        return  recipientName;
    }

    public void setRecipientTyping(boolean isTyping) {
        this.isRecipientTyping = isTyping;
    }

    public boolean getRecipientTyping() {
        return this.isRecipientTyping;
    }

    public String getMonthDay() { return monthDay; }

    public boolean isPhotoMsg() {
        return isPhotoMsg;
    }

    public void setPhotoMsg(boolean photoMsg) {
        isPhotoMsg = photoMsg;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    public int getIsPhoto() {
        return isPhoto;
    }

    public void setIsPhoto(int isPhoto) {
        this.isPhoto = isPhoto;
    }
}
