package tcss450.uw.edu.hitmeupv2.WebService;

/**
 * Jason Thai
 * Shema Rezanejad
 *
 * Used to read user information from the web service.
 */

public class User {
    /** Store username. */
    private String username;
    /** Store password. */
    private String password;

    /*
     * Message sent from the web service, can be "Success" which means user found
     * or "Error" which means user not found
     */
    private String message;
    /**
     * Store userId from user.
     */
    private String userId;

    /** store the path of the profile picture for a user. */
    private String profileImgPath;

    /**
     * Getter
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter.
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter.
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Return the path of the image
     * @return profileImgPath
     */
    public String getProfileImgPath() { return profileImgPath; }
}

