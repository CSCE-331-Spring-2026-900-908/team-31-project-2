package com.example.team31project2;

public final class SceneConfig {
    public static final double APP_WIDTH = 1024;
    public static final double APP_HEIGHT = 768;
    public static final double LOGIN_WIDTH = 400;
    public static final double LOGIN_HEIGHT = 580;

    private SceneConfig() {
    }

    public static boolean isLoginView(String fxmlFile) {
        return "login-view.fxml".equals(fxmlFile);
    }
}
