package com.example.team31project2;

import java.io.IOException;
import org.kordamp.bootstrapfx.BootstrapFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

public class InventoryViewController {

    @FXML
    private MenuBar menuBar;

    @FXML
    void handleNavigateOrdering(ActionEvent event) {
        navigateTo("ordering-view.fxml");
    }

    @FXML
    void handleNavigateMenuEdit(ActionEvent event) {
        navigateTo("menu-edit-view.fxml");
    }

    @FXML
    void handleNavigateEmployees(ActionEvent event) {
        navigateTo("employee-list-view.fxml");
    }

    @FXML
    void handleNavigateInventory(ActionEvent event) {
        navigateTo("inventory-view.fxml");
    }

    @FXML
    void handleNavigateTrends(ActionEvent event) {
        navigateTo("reports-view.fxml");
    }

    @FXML
    void handleSignOut(ActionEvent event) {
        UserSession.clear();
        navigateTo("login-view.fxml");
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) menuBar.getScene().getWindow();
            double width = SceneConfig.isLoginView(fxmlFile) ? SceneConfig.LOGIN_WIDTH : SceneConfig.APP_WIDTH;
            double height = SceneConfig.isLoginView(fxmlFile) ? SceneConfig.LOGIN_HEIGHT : SceneConfig.APP_HEIGHT;
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
