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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeListController {

    @FXML private TextField searchField;
    @FXML private VBox employeeList;

    private final List<HBox> allRows = new ArrayList<>();

    @FXML
    public void initialize() {
        loadEmployees();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterRows(newVal));
    }

    private void loadEmployees() {
        String query = "SELECT id, name, role, is_active FROM employee ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            allRows.clear();
            employeeList.getChildren().clear();

            while (rs.next()) {
                int id       = rs.getInt("id");
                String name  = rs.getString("name");
                String role  = rs.getString("role");
                boolean isActive = rs.getBoolean("is_active");
                HBox row = buildRow(id, name, role != null ? role : "", isActive);
                allRows.add(row);
                employeeList.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox buildRow(int id, String name, String role, boolean isActive) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-border-color: transparent transparent #e0e0e0 transparent;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-padding: 10 0 10 0;"
        );

        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(300.0);
        nameLabel.setStyle(isActive
            ? "-fx-font-size: 14; -fx-text-fill: #333333;"
            : "-fx-font-size: 14; -fx-text-fill: #999999;");

        Label roleLabel = new Label(isActive ? role : role + " (Disabled)");
        roleLabel.setPrefWidth(150.0);
        roleLabel.setStyle(isActive
            ? "-fx-font-size: 14; -fx-text-fill: #555555;"
            : "-fx-font-size: 14; -fx-text-fill: #999999;");

        Button editBtn = new Button("✎");
        editBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #555555;" +
            "-fx-border-radius: 50;" +
            "-fx-background-radius: 50;" +
            "-fx-padding: 4 9 4 9;" +
            "-fx-font-size: 13;"
        );
        editBtn.setOnAction(e -> handleEditEmployee(id, name, role));

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
        removeBtn.setOnAction(e -> handleRemoveEmployee(id, name, row));
        HBox actions;
        if (!isActive) {
            Button enableBtn = new Button("↺");
            enableBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #2f8f2f;" +
                "-fx-border-radius: 50;" +
                "-fx-background-radius: 50;" +
                "-fx-padding: 4 9 4 9;" +
                "-fx-font-size: 13;" +
                "-fx-text-fill: #2f8f2f;"
            );
            enableBtn.setOnAction(e -> handleEnableEmployee(id, name));
            actions = new HBox(8, editBtn, enableBtn);
        } else {
            actions = new HBox(8, editBtn, removeBtn);
        }
        actions.setAlignment(Pos.CENTER_LEFT);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        spacer.setPrefWidth(150.0);

        row.getChildren().addAll(nameLabel, roleLabel, spacer, actions);
        return row;
    }

    private void filterRows(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        employeeList.getChildren().clear();
        for (HBox row : allRows) {
            Label nameLabel = (Label) row.getChildren().get(0);
            Label roleLabel = (Label) row.getChildren().get(1);
            if (nameLabel.getText().toLowerCase().contains(lower)
                    || roleLabel.getText().toLowerCase().contains(lower)) {
                employeeList.getChildren().add(row);
            }
        }
    }

    private void handleEditEmployee(int id, String currentName, String currentRole) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");
        dialog.setHeaderText("Edit " + currentName);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField nameField = new TextField(currentName);
        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton cashierBtn = createRoleButton("Cashier", roleGroup);
        ToggleButton managerBtn = createRoleButton("Manager", roleGroup);
        HBox roleButtons = new HBox(8, cashierBtn, managerBtn);

        if ("Manager".equalsIgnoreCase(currentRole)) {
            managerBtn.setSelected(true);
        } else {
            cashierBtn.setSelected(true);
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(roleButtons, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newName = nameField.getText().trim();
            String newRole = getSelectedRole(roleGroup);
            if (!newName.isEmpty() && newRole != null) {
                String sql = "UPDATE employee SET name = ?, role = ? WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, newName);
                    pstmt.setString(2, newRole);
                    pstmt.setInt(3, id);
                    pstmt.executeUpdate();
                    loadEmployees();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleRemoveEmployee(int id, String name, HBox row) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Employee");
        confirm.setHeaderText("Remove " + name + "?");
        confirm.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM employee WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                allRows.remove(row);
                employeeList.getChildren().remove(row);
            } catch (SQLException e) {
                if (isForeignKeyViolation(e)) {
                    disableEmployee(id);
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Employee Disabled");
                    info.setHeaderText("Cannot delete " + name);
                    info.setContentText("This employee has related records. They were disabled instead.");
                    info.showAndWait();
                    loadEmployees();
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void handleAddEmployee(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Employee");
        dialog.setHeaderText("New Employee");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton cashierBtn = createRoleButton("Cashier", roleGroup);
        ToggleButton managerBtn = createRoleButton("Manager", roleGroup);
        cashierBtn.setSelected(true);
        HBox roleButtons = new HBox(8, cashierBtn, managerBtn);
        TextField pinField = new TextField();
        pinField.setPromptText("PIN");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(roleButtons, 1, 1);
        grid.add(new Label("PIN:"), 0, 2);
        grid.add(pinField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText().trim();
            String role = getSelectedRole(roleGroup);
            String pin  = pinField.getText().trim();
            if (!name.isEmpty() && role != null) {
                String sql = "INSERT INTO employee (name, role, pin_hash) VALUES (?, ?, ?)";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, role);
                    pstmt.setString(3, pin);
                    pstmt.executeUpdate();
                    loadEmployees();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ToggleButton createRoleButton(String label, ToggleGroup group) {
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

    private String getSelectedRole(ToggleGroup group) {
        if (group.getSelectedToggle() == null) {
            return null;
        }
        return ((ToggleButton) group.getSelectedToggle()).getText();
    }

    private boolean isForeignKeyViolation(SQLException exception) {
        SQLException current = exception;
        while (current != null) {
            if ("23503".equals(current.getSQLState())) {
                return true;
            }
            String message = current.getMessage();
            if (message != null && message.toLowerCase().contains("foreign key")) {
                return true;
            }
            current = current.getNextException();
        }
        return false;
    }

    private void disableEmployee(int id) {
        String sql = "UPDATE employee SET is_active = FALSE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleEnableEmployee(int id, String name) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Enable Employee");
        confirm.setHeaderText("Enable " + name + "?");
        confirm.setContentText("This employee will be able to use the system again.");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "UPDATE employee SET is_active = TRUE WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                loadEmployees();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Navigation

    @FXML
    void handleSignOut(ActionEvent event) {
        UserSession.clear();
        navigateTo("login-view.fxml");
    }

    @FXML
    void handleNavigateOrdering(ActionEvent event) {
        navigateTo("ordering-view.fxml");
    }

    @FXML
    void handleNavigateMenuEdit(ActionEvent event) {
        navigateTo("menu-edit-view.fxml");
    }

    @FXML
    void handleNavigateInventory(ActionEvent event) {
        navigateTo("inventory-view.fxml");
    }

    @FXML
    void handleNavigateReports(ActionEvent event) {
        navigateTo("reports-view.fxml");
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) employeeList.getScene().getWindow();
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
