package tcss450.uw.edu.hitmeupv2.WebService;

/**
 * Used to read user information from the web service
 */

public class User {

    private String username;
    private String password;

    /*
     * Message sent from the web service, can be "Success" which means user found
     * or "Error" which means user not found
     */
    private String message;
    private String userId;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

}

