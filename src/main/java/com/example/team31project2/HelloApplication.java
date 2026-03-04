package com.example.team31project2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import org.kordamp.bootstrapfx.BootstrapFX;

/**
 * JavaFX application entry point that loads the login view.
 *
 * @author Team-31
 */
public class HelloApplication extends Application {
    @Override
    /**
     * Starts the JavaFX application and shows the login scene.
     *
     * @param stage primary application stage
     * @throws IOException if the FXML resource cannot be loaded
     */
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), SceneConfig.LOGIN_WIDTH, SceneConfig.LOGIN_HEIGHT);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("POS Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}