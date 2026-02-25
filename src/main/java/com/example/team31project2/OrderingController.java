package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderingController {

    @FXML private TextArea orderInfo;

    @FXML void handleNavigateMenuEdit(ActionEvent event)  { navigateTo("menu-edit-view.fxml"); }
    @FXML void handleNavigateEmployees(ActionEvent event) { navigateTo("employee-list-view.fxml"); }
    @FXML void handleNavigateInventory(ActionEvent event) { navigateTo("inventory-view.fxml"); }
    @FXML void handleNavigateTrends(ActionEvent event)    { navigateTo("order-trend.fxml"); }
    @FXML void handleSignOut(ActionEvent event)           { navigateTo("login-view.fxml"); }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) orderInfo.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
