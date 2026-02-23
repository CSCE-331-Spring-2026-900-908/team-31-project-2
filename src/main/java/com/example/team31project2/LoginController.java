package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

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

        boolean isValid = verifyPin(pin);
        
        if (isValid) {
            System.out.println("Login successful!");
            // TODO: Load the next scene (e.g., POS terminal)
            
            currentPin.setLength(0);
            pinField.setText("");
        } else {
            showError("Invalid PIN.");
            currentPin.setLength(0);
            pinField.setText("");
        }
    }

    private boolean verifyPin(String pin) {
        String query = "SELECT * FROM employee WHERE pin_hash = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, pin);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database connection error.");
        }
        
        return false;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
