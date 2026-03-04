package com.example.team31project2;

/**
 * Configuration class that stores application scene dimensions
 * and utility methods related to scene management.
 *
 * @author team 31
 */
public final class SceneConfig {

    public static final double APP_WIDTH = 1024;
    public static final double APP_HEIGHT = 768;
    public static final double LOGIN_WIDTH = 400;
    public static final double LOGIN_HEIGHT = 580;

    private SceneConfig() {
    }

    /**
     * Determines whether the given FXML file corresponds to the login view.
     *
     * @param fxmlFile the name of the FXML file
     * @return true if the file is the login view; false otherwise
     */
    public static boolean isLoginView(String fxmlFile) {
        return "login-view.fxml".equals(fxmlFile);
    }
}