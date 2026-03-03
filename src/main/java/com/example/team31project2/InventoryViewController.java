package com.example.team31project2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.beans.binding.Bindings;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InventoryViewController {

    @FXML private MenuBar menuBar;
    @FXML private VBox inventoryList;
    @FXML private TextField searchField;

    private final List<HBox> allRows = new ArrayList<>();
    private final List<InventoryItem> allItems = new ArrayList<>();

    public void initialize() {
        loadInventoryFromDB();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterRows(newVal));
    }

    private void loadInventoryFromDB() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            InventoryDAO dao = new InventoryDAO(conn);
            List<InventoryItem> items = dao.getAllItems();
            allRows.clear();
            allItems.clear();
            inventoryList.getChildren().clear();
            for (InventoryItem item : items) {
                HBox row = buildRow(item);
                allRows.add(row);
                allItems.add(item);
                inventoryList.getChildren().add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox buildRow(InventoryItem item) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-border-color: transparent transparent #e0e0e0 transparent;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-padding: 10 0 10 0;"
        );

        Label idLabel = new Label(String.valueOf(item.getItemId()));
        idLabel.setPrefWidth(60.0);
        idLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333333;");

        Label nameLabel = new Label(item.getItemName());
        nameLabel.setPrefWidth(200.0);
        nameLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333333;");

        Label currentLabel = new Label();
        currentLabel.setPrefWidth(80.0);
        currentLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");
        currentLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("%.0f", item.quantityProperty().get()),
                item.quantityProperty()
            )
        );

        Label targetLabel = new Label();
        targetLabel.setPrefWidth(80.0);
        targetLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");
        targetLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("%.0f", item.targetProperty().get()),
                item.targetProperty()
            )
        );

        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(155.0);
        bar.progressProperty().bind(item.fillRatioProperty());
        setProgressBarColor(bar, item.fillRatioProperty().get());
        item.fillRatioProperty().addListener((obs, oldV, newV) -> setProgressBarColor(bar, newV.doubleValue()));

        HBox fillBox = new HBox(bar);
        fillBox.setAlignment(Pos.CENTER_LEFT);
        fillBox.setPrefWidth(180.0);

        Button editBtn = new Button("✎");
        editBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #555555;" +
            "-fx-border-radius: 50;" +
            "-fx-background-radius: 50;" +
            "-fx-padding: 4 9 4 9;" +
            "-fx-font-size: 13;"
        );
        editBtn.setOnAction(e -> handleEditItem(item));

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
        removeBtn.setOnAction(e -> handleRemoveItem(item));

        HBox actions = new HBox(8, editBtn, removeBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(idLabel, nameLabel, currentLabel, targetLabel, fillBox, actions);
        return row;
    }

    @FXML
    private void showAddPopup() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Inventory Item");
        dialog.setHeaderText("New Inventory Item");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Item name");
        TextField currentField = new TextField();
        currentField.setPromptText("Quantity");
        TextField targetField = new TextField();
        targetField.setPromptText("Target");

        ToggleGroup unitGroup = new ToggleGroup();
        ToggleButton gramsBtn = createUnitButton("grams", unitGroup);
        ToggleButton kgBtn    = createUnitButton("kg",    unitGroup);
        ToggleButton litersBtn = createUnitButton("liters", unitGroup);
        ToggleButton gallonsBtn = createUnitButton("gallons", unitGroup);
        kgBtn.setSelected(true);
        HBox unitButtons = new HBox(8, gramsBtn, kgBtn, litersBtn, gallonsBtn);

        grid.add(new Label("Name:"),    0, 0); grid.add(nameField,    1, 0);
        grid.add(new Label("Current:"), 0, 1); grid.add(currentField, 1, 1);
        grid.add(new Label("Target:"),  0, 2); grid.add(targetField,  1, 2);
        grid.add(new Label("Unit:"),    0, 3); grid.add(unitButtons,  1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText().trim();
            String unit = getSelectedUnit(unitGroup);
            double quantity, target;
            try {
                quantity = Double.parseDouble(currentField.getText().trim());
                target   = Double.parseDouble(targetField.getText().trim());
            } catch (NumberFormatException e) {
                return;
            }
            if (name.isEmpty() || unit == null || quantity < 0 || target < 0) return;

            Integer id = insertItemInDb(name, quantity, null, target, unit);
            if (id == null) return;

            loadInventoryFromDB();
            filterRows(searchField.getText());
        }
    }

    private void handleEditItem(InventoryItem item) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Inventory Item");
        dialog.setHeaderText("Edit " + item.getItemName());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField currentField = new TextField(String.format("%.0f", item.getQuantity()));
        TextField targetField  = new TextField(String.format("%.0f", item.getTarget()));

        grid.add(new Label("Current:"), 0, 0); grid.add(currentField, 1, 0);
        grid.add(new Label("Target:"),  0, 1); grid.add(targetField,  1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                double newQty    = Double.parseDouble(currentField.getText().trim());
                double newTarget = Double.parseDouble(targetField.getText().trim());
                if (newQty >= 0 && newTarget >= 0) {
                    if (updateQuantityInDb(item.getItemId(), newQty))    item.setQuantity(newQty);
                    if (updateTargetInDb(item.getItemId(), newTarget))   item.setTarget(newTarget);
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    private void handleRemoveItem(InventoryItem item) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Item");
        confirm.setHeaderText("Remove " + item.getItemName() + "?");
        confirm.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (removeFromDB(item.getItemId())) {
                loadInventoryFromDB();
                filterRows(searchField.getText());
            }
        }
    }

    private void filterRows(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        inventoryList.getChildren().clear();
        for (int i = 0; i < allRows.size(); i++) {
            if (allItems.get(i).getItemName().toLowerCase().contains(lower)) {
                inventoryList.getChildren().add(allRows.get(i));
            }
        }
    }

    private ToggleButton createUnitButton(String label, ToggleGroup group) {
        ToggleButton button = new ToggleButton(label);
        button.setToggleGroup(group);
        button.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #bfbfbf;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 6 14 6 14;"
        );
        button.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                button.setStyle(
                    "-fx-background-color: #333333;" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #333333;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 6 14 6 14;"
                );
            } else {
                button.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #bfbfbf;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 6 14 6 14;"
                );
            }
        });
        return button;
    }

    private String getSelectedUnit(ToggleGroup group) {
        if (group.getSelectedToggle() == null) return null;
        return ((ToggleButton) group.getSelectedToggle()).getText();
    }

    private void setProgressBarColor(ProgressBar bar, double ratio) {
        if (ratio >= 0.81) {
            bar.setStyle("-fx-accent: #2ecc71;");
        } else if (ratio >= 0.41) {
            bar.setStyle("-fx-accent: #f1c40f;");
        } else if (ratio >= 0.16) {
            bar.setStyle("-fx-accent: #e67e22;");
        } else {
            bar.setStyle("-fx-accent: #e74c3c;");
        }
    }

    private Integer insertItemInDb(String name, double quantity, java.time.LocalDateTime exp, double target, String unitType) {
        String sql = "INSERT INTO inventory (item_name, quantity, unit_type, expiration_date, target_val) VALUES (?, ?, ?, ?, ?) RETURNING item_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, quantity);
            ps.setString(3, unitType);
            if (exp == null) ps.setTimestamp(4, null);
            else ps.setTimestamp(4, java.sql.Timestamp.valueOf(exp));
            ps.setDouble(5, target);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("item_id");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean updateQuantityInDb(int itemId, Double newCurrent) {
        String sql = "UPDATE inventory SET quantity = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newCurrent);
            ps.setInt(2, itemId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateTargetInDb(int itemId, Double newTarget) {
        String sql = "UPDATE inventory SET target_val = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newTarget);
            ps.setInt(2, itemId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean removeFromDB(int id) {
        String sql = "DELETE FROM inventory WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Navigation

    @FXML void handleNavigateOrdering(ActionEvent event)  { navigateTo("ordering-view.fxml"); }
    @FXML void handleNavigateMenuEdit(ActionEvent event)   { navigateTo("menu-edit-view.fxml"); }
    @FXML void handleNavigateEmployees(ActionEvent event)  { navigateTo("employee-list-view.fxml"); }
    @FXML void handleNavigateInventory(ActionEvent event)  { navigateTo("inventory-view.fxml"); }
    @FXML void handleNavigateReports(ActionEvent event)    { navigateTo("reports-view.fxml"); }

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
            double width  = SceneConfig.isLoginView(fxmlFile) ? SceneConfig.LOGIN_WIDTH  : SceneConfig.APP_WIDTH;
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
