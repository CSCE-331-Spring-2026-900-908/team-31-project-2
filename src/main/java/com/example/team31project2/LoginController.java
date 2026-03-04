package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller class for the Login screen.
 * Handles user authentication using a PIN-based login system.
 * Includes functionality for number pad interaction and database verification.
 * 
 * @author Team 31
 */
public class LoginController {

    @FXML
    private PasswordField pinField;

    @FXML
    private Label errorLabel;

    private StringBuilder currentPin = new StringBuilder();

    /**
     * Initializes the controller class.
     * Sets the error label to be invisible upon startup.
     */
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    /**
     * Handles the number pad button clicks.
     * Updates the pin field with the entered number, clears it, or initiates login.
     * 
     * @param event The action event triggered by the button click.
     */
    @FXML
    void handleNumberPad(ActionEvent event) {
        Button source = (Button) event.getSource();
        String text = source.getText();

        if (text.equals("C")) {
            currentPin.setLength(0);
            pinField.setText("");
            errorLabel.setVisible(false);
        } else if (text.equals(">")) {
            handleLogin();
        } else {
            if (currentPin.length() < 6) {
                currentPin.append(text);
                pinField.setText(currentPin.toString());
                errorLabel.setVisible(false);
            }
        }
    }
    
    /**
     * Processes the login attempt.
     * Verifies the entered PIN against the database and navigates to the ordering screen if successful.
     */
    private void handleLogin() {
        String pin = currentPin.toString();
        if (pin.isEmpty()) {
            showError("Please enter a PIN.");
            return;
        }

        Employee user = authenticateUser(pin);
        
        if (user != null) {
            System.out.println("Login successful for user: " + user.getName());
            UserSession.setCurrentUser(user);
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ordering-view.fxml"));
                Parent root = loader.load();
                
                OrderController orderController = loader.getController();
                orderController.setUser(user);
                
                Stage stage = (Stage) pinField.getScene().getWindow();
                Scene scene = new Scene(root, SceneConfig.APP_WIDTH, SceneConfig.APP_HEIGHT);
                scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                stage.setScene(scene);
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading the next page.");
            }
            
            currentPin.setLength(0);
            pinField.setText("");
        } else {
            showError("Invalid PIN.");
            currentPin.setLength(0);
            pinField.setText("");
        }
    }
    
    /**
     * Authenticates the user by checking the PIN against the database.
     * 
     * @param pin The PIN entered by the user.
     * @return The Employee object if authentication is successful, null otherwise.
     */
    private Employee authenticateUser(String pin) {
        String query = "SELECT id, name, role, pin_hash FROM employee WHERE pin_hash = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, pin);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = rs.getString("role");
                String pinHash = rs.getString("pin_hash");
                
                return new Employee(id, name, role, pinHash);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database connection error.");
        }
        
        return null;
    }
    
    /**
     * Displays an error message to the user.
     * 
     * @param message The error message to display.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
