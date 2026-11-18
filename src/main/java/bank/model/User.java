package bank.model;

public class User {
    private final String userId;
    private final String username;
    private final String password;
    private final String role;          // CUSTOMER, TELLER or SUPERVISOR
    private boolean loggedIn;

    public User(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(String username, String password) {
        loggedIn = this.username.equals(username) && this.password.equals(password);
        return loggedIn;
    }

    public void logout() {
        loggedIn = false;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isLoggedIn() { return loggedIn; }
}