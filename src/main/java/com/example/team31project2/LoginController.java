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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private PasswordField pinField;

    @FXML
    private Label errorLabel;

    private StringBuilder currentPin = new StringBuilder();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

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

    private void handleLogin() {
        String pin = currentPin.toString();
        if (pin.isEmpty()) {
            showError("Please enter a PIN.");
            return;
        }

        Employee user = authenticateUser(pin);
        
        if (user != null) {
            System.out.println("Login successful for user: " + user.getName());
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ordering-view.fxml"));
                Parent root = loader.load();
                
                // OrderingController orderingController = loader.getController();
                // orderingController.setUser(user);
                
                Stage stage = (Stage) pinField.getScene().getWindow();
                // Standard dimension for the POS could be larger, e.g., 1024x768
                stage.setScene(new Scene(root, 1024, 768));
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

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
