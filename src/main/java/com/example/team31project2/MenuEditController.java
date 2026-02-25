package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
import java.util.Optional;

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
        String query = "SELECT product_id, name, base_price, category_name FROM product ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            allRows.clear();
            menuItemsList.getChildren().clear();

            while (rs.next()) {
                int id          = rs.getInt("product_id");
                String name     = rs.getString("name");
                double price    = rs.getDouble("base_price");
                String category = rs.getString("category_name");
                HBox row = buildRow(id, name, price, category != null ? category : "");
                allRows.add(row);
                menuItemsList.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox buildRow(int id, String name, double price, String category) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-border-color: transparent transparent #e0e0e0 transparent;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-padding: 10 0 10 0;"
        );

        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(300.0);
        nameLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333333;");

        Label categoryLabel = new Label(category);
        categoryLabel.setPrefWidth(200.0);
        categoryLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

        Label priceLabel = new Label(String.format("$%.2f", price));
        priceLabel.setPrefWidth(100.0);
        priceLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

        Button editBtn = new Button("✎");
        editBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #555555;" +
            "-fx-border-radius: 50;" +
            "-fx-background-radius: 50;" +
            "-fx-padding: 4 9 4 9;" +
            "-fx-font-size: 13;"
        );
        editBtn.setOnAction(e -> handleEditMenuItem(id, name, price, category));

        Button removeBtn = new Button("✕");
        removeBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #cc3333;" +
            "-fx-border-radius: 50;" +
            "-fx-background-radius: 50;" +
            "-fx-padding: 4 9 4 9;" +
            "-fx-font-size: 13;" +
            "-fx-text-fill: #cc3333;"
        );
        removeBtn.setOnAction(e -> handleRemoveMenuItem(id, name, row));

        HBox actions = new HBox(8, editBtn, removeBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(nameLabel, categoryLabel, priceLabel, actions);
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

    private void handleEditMenuItem(int id, String currentName, double currentPrice, String currentCategory) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Menu Item");
        dialog.setHeaderText("Edit " + currentName);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField nameField     = new TextField(currentName);
        TextField priceField    = new TextField(String.valueOf(currentPrice));
        TextField categoryField = new TextField(currentCategory);

        grid.add(new Label("Name:"),     0, 0); grid.add(nameField,     1, 0);
        grid.add(new Label("Price:"),    0, 1); grid.add(priceField,    1, 1);
        grid.add(new Label("Category:"), 0, 2); grid.add(categoryField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newName     = nameField.getText().trim();
            String newCategory = categoryField.getText().trim();
            if (!newName.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceField.getText().trim());
                    String sql = "UPDATE product SET name = ?, base_price = ?, category_name = ? WHERE product_id = ?";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, newName);
                        pstmt.setDouble(2, newPrice);
                        pstmt.setString(3, newCategory);
                        pstmt.setInt(4, id);
                        pstmt.executeUpdate();
                        loadMenuItems();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the price.").showAndWait();
                }
            }
        }
    }

    private void handleRemoveMenuItem(int id, String name, HBox row) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Menu Item");
        confirm.setHeaderText("Remove " + name + "?");
        confirm.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                try (PreparedStatement del = conn.prepareStatement(
                        "DELETE FROM productmodifier WHERE product_id = ?")) {
                    del.setInt(1, id);
                    del.executeUpdate();
                }
                try (PreparedStatement del = conn.prepareStatement(
                        "DELETE FROM productingredient WHERE product_id = ?")) {
                    del.setInt(1, id);
                    del.executeUpdate();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "DELETE FROM product WHERE product_id = ?")) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                }
                allRows.remove(row);
                menuItemsList.getChildren().remove(row);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,
                    "Cannot delete \"" + name + "\" because it is referenced in existing orders.\n" +
                    "Consider deactivating it instead.").showAndWait();
            }
        }
    }

    @FXML
    void handleAddMenuItem(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Menu Item");
        dialog.setHeaderText("New Menu Item");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField nameField     = new TextField();
        nameField.setPromptText("Name");
        TextField priceField    = new TextField();
        priceField.setPromptText("0.00");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        grid.add(new Label("Name:"),     0, 0); grid.add(nameField,     1, 0);
        grid.add(new Label("Price:"),    0, 1); grid.add(priceField,    1, 1);
        grid.add(new Label("Category:"), 0, 2); grid.add(categoryField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name     = nameField.getText().trim();
            String category = categoryField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceField.getText().trim());
                    String sql = "INSERT INTO product (name, base_price, category_name) VALUES (?, ?, ?)";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setDouble(2, price);
                        pstmt.setString(3, category);
                        pstmt.executeUpdate();
                        loadMenuItems();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the price.").showAndWait();
                }
            }
        }
    }

    // Navigation

    @FXML void handleSignOut(ActionEvent event)           { navigateTo("login-view.fxml"); }
    @FXML void handleNavigateOrdering(ActionEvent event)  { navigateTo("ordering-view.fxml"); }
    @FXML void handleNavigateEmployees(ActionEvent event) { navigateTo("employee-list-view.fxml"); }
    @FXML void handleNavigateInventory(ActionEvent event) { navigateTo("inventory-view.fxml"); }
    @FXML void handleNavigateTrends(ActionEvent event)    { navigateTo("order-trend.fxml"); }

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
