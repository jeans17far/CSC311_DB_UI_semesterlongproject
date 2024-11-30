package service;

import java.util.prefs.Preferences;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Singleton class that manages user session data, including username and role.
 * Provides functionality to save, retrieve, and clear session credentials.
 */
public class UserSession {

    private static volatile UserSession instance;
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Preferences preferences;
    private final String username;
    private final String role;

    /**
     * Private constructor to initialize a user session.
     *
     * @param username the username of the logged-in user
     * @param role     the role of the logged-in user
     */
    private UserSession(String username, String role) {
        this.username = username;
        this.role = role;
        this.preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    /**
     * Retrieves the singleton instance of the UserSession. Creates a new instance if none exists.
     *
     * @param username the username of the user
     * @param role     the role of the user
     * @return the singleton instance of UserSession
     */
    public static UserSession getInstance(String username, String role) {
        UserSession localInstance = instance;
        if (localInstance == null) {
            lock.writeLock().lock();
            try {
                localInstance = instance;
                if (localInstance == null) {
                    instance = new UserSession(username, role);
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return instance;
    }

    /**
     * Saves the user's credentials in the preferences store.
     *
     * @param username the username to save
     * @param password the password to save
     */
    public void saveCredentials(String username, String password) {
        preferences.put("username", username);
        preferences.put("password", password);
    }

    /**
     * Retrieves the username from the preferences store.
     *
     * @return the saved username or null if none exists
     */
    public String getUsername() {
        return preferences.get("username", null);
    }

    /**
     * Retrieves the password from the preferences store.
     *
     * @return the saved password or null if none exists
     */
    public String getPassword() {
        return preferences.get("password", null);
    }

    /**
     * Clears the stored credentials from the preferences store.
     */
    public void clearCredentials() {
        preferences.remove("username");
        preferences.remove("password");
    }

    /**
     * Retrieves the role associated with this session.
     *
     * @return the role of the user
     */
    public String getRole() {
        return role;
    }

    /**
     * Clears the current user session and resets the instance.
     */
    public static void cleanUserSession() {
        lock.writeLock().lock();
        try {
            if (instance != null) {
                instance.clearCredentials();
                instance = null;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Returns a string representation of the UserSession.
     *
     * @return a formatted string containing session details
     */
    @Override
    public String toString() {
        return "UserSession {username='" + username + "', role='" + role + "'}";
    }
}
