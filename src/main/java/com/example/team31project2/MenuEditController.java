package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuEditController {

    @FXML private TextField searchField;
    @FXML private VBox menuItemsList;

    private final List<HBox> allRows = new ArrayList<>();

    @FXML
    public void initialize() {
        loadMenuItems();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterRows(newVal));
    }

    private void loadMenuItems() {
        String query = "SELECT name, category FROM menu_item ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            allRows.clear();
            menuItemsList.getChildren().clear();

            while (rs.next()) {
                String name     = rs.getString("name");
                String category = rs.getString("category");
                HBox row = buildRow(name, category != null ? category : "");
                allRows.add(row);
                menuItemsList.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox buildRow(String name, String category) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-border-color: transparent transparent #e0e0e0 transparent;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-padding: 10 0 10 0;"
        );

        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(420.0);
        nameLabel.setStyle("-fx-font-size: 14;");

        Label categoryLabel = new Label(category);
        categoryLabel.setPrefWidth(220.0);
        categoryLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

        row.getChildren().addAll(nameLabel, categoryLabel);
        return row;
    }

    private void filterRows(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        menuItemsList.getChildren().clear();
        for (HBox row : allRows) {
            Label nameLabel     = (Label) row.getChildren().get(0);
            Label categoryLabel = (Label) row.getChildren().get(1);
            if (nameLabel.getText().toLowerCase().contains(lower)
                    || categoryLabel.getText().toLowerCase().contains(lower)) {
                menuItemsList.getChildren().add(row);
            }
        }
    }

    // Navigation Drop Down

    @FXML
    void handleSignOut(ActionEvent event) {
        navigateTo("login-view.fxml");
    }

    @FXML
    void handleNavigateOrdering(ActionEvent event) {
        navigateTo("ordering-view.fxml");
    }

    @FXML
    void handleNavigateEmployees(ActionEvent event) {
        // TODO: replace with employee-view.fxml once that view is created
    }

    @FXML
    void handleNavigateInventory(ActionEvent event) {
        navigateTo("inventory-view.fxml");
    }

    @FXML
    void handleNavigateTrends(ActionEvent event) {
        navigateTo("order-trend.fxml");
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            switchScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(Parent root) {
        Stage stage = (Stage) menuItemsList.getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.centerOnScreen();
    }
}
