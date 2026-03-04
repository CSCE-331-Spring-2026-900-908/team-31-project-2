package com.example.team31project2;

/**
 * Manages the current authenticated user session.
 * <p>
 * This utility class stores and provides access to the
 * currently logged-in {@link Employee}.
 *
 * @author team 31
 */
public final class UserSession {
    private static Employee currentUser;

    private UserSession() {
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the current {@link Employee}, or null if no user is logged in
     */
    public static Employee getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current logged-in user.
     *
     * @param user the {@link Employee} to store as the current user
     */
    public static void setCurrentUser(Employee user) {
        currentUser = user;
    }

    /**
     * Clears the current session by removing the stored user.
     *
     * @return void
     */
    public static void clear() {
        currentUser = null;
    }
}