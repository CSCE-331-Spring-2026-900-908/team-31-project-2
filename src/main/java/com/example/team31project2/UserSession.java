package com.example.team31project2;

public final class UserSession {
    private static Employee currentUser;

    private UserSession() {
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Employee user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
