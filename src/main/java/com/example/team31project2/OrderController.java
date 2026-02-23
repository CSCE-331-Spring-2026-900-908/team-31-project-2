package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class OrderController {
    @FXML
    private TextArea orderInfo;

    @FXML
    private TextArea orderTotal;

    @FXML
    private TextField searchBar;

    @FXML private Button menuItem01;
    @FXML private Button menuItem02;
    @FXML private Button menuItem03;
    @FXML private Button menuItem04;
    @FXML private Button menuItem05;
    @FXML private Button menuItem06;
    @FXML private Button menuItem07;
    @FXML private Button menuItem08;
    @FXML private Button menuItem09;
    @FXML private Button menuItem10;
    @FXML private Button menuItem11;
    @FXML private Button menuItem12;
    @FXML private Button menuItem13;
    @FXML private Button menuItem14;
    @FXML private Button menuItem15;
    @FXML private Button menuItem16;
    @FXML private Button menuItem17;
    @FXML private Button menuItem18;
    @FXML private Button menuItem19;
    @FXML private Button menuItem20;
    @FXML private Button menuItem21;
    @FXML private Button menuItem22;
    @FXML private Button menuItem23;
    @FXML private Button menuItem24;
    @FXML private Button menuItem25;
    @FXML private Button menuItem26;
    @FXML private Button menuItem27;
    @FXML private Button menuItem28;
    @FXML private Button menuItem29;
    @FXML private Button menuItem30;
    
    private int employeeID = 1;
    private int orderID;
    private List<Integer> detailIDs = new ArrayList<Integer>();
    private List<Button> menuButtons;
    private StringBuilder category = new StringBuilder();
    private StringBuilder currentPin = new StringBuilder();

    @FXML
    public void initialize() {
        menuButtons = List.of(menuItem01, menuItem02, menuItem03, menuItem04, menuItem05, menuItem06,
                              menuItem07, menuItem08, menuItem09, menuItem10, menuItem11, menuItem12,
                              menuItem13, menuItem14, menuItem15, menuItem16, menuItem17, menuItem18,
                              menuItem19, menuItem20, menuItem21, menuItem22, menuItem23, menuItem24,
                              menuItem25, menuItem26, menuItem27, menuItem28, menuItem29, menuItem30);
        // String[] orderDetails = {String.valueOf(employee_id), LocalDateTime.now().toString(), "0", "0"};
        // String query = "INSERT INTO \"order\" (employee_id, created_at, total_tax, total_final) VALUES (" +
        //             String.join(", ", String.valueOf(employee_id), LocalDateTime.now().toString(), "0", "0");
        String query = "INSERT INTO \"order\" (employee_id, created_at, total_tax, total_final) VALUES (?, ?, 0, 0) RETURNING id;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, employeeID);
            pstmt.setObject(2, LocalDateTime.now());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                orderID = rs.getInt("id");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "SELECT name FROM product ORDER BY id;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            int i = 0;
            while (rs.next() && i < 30) {
                menuButtons.get(i).setText(rs.getString("name"));
                i++;
            }
            for ( ; i < 30; i++) {
                menuButtons.get(i).setVisible(false);;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toggleCategory(ActionEvent event) {
        Button source = (Button) event.getSource();
        String text = source.getText();
        
        if(category.indexOf(text) >= 0) {
            category.delete(category.indexOf(text), category.indexOf(text) + text.length());
        } else {
            category.append(text);
        }
        // if (category.contains(text)) {
        //     category = category.substring(0, category.indexOf(text)) + category.substring(category.indexOf(text) + text.length());
        // } else {
        //     category += text;
        // }
    }

    @FXML
    void addDrink(ActionEvent event) {
        Button source = (Button) event.getSource();
        // String text = source.getText();
        // int productID = Integer.valueOf(source.getId().substring(8));
        // String productName = "";
        // float productPrice = 0;

        // String query = "SELECT name, base_price FROM product WHERE id = ?;";

        // try (Connection conn = DatabaseConnection.getConnection();
        //      PreparedStatement pstmt = conn.prepareStatement(query)) {
            
        //     pstmt.setInt(1, productID);
        //     ResultSet rs = pstmt.executeQuery();
            
        //     if (rs.next()) {
        //         productName = rs.getString("name");
        //         productPrice = rs.getFloat("base_price");
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }

        String productName = source.getText();
        int productID = 0;
        float productPrice = 0;

        String query = "SELECT id, base_price FROM product WHERE name = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                productID = rs.getInt("id");
                productPrice = rs.getFloat("base_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO \"order_detail\" (order_id, product_id, sold_price, snapshot_name) VALUES (?, ?, ?, ?) RETURNING id;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, orderID);
            pstmt.setInt(2, productID);
            pstmt.setFloat(3, productPrice);
            pstmt.setString(4, productName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                detailIDs.add(rs.getInt("id"));
                updateOrderInfo();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search(ActionEvent event) {
        Label source = (Label) event.getSource();
        String text = source.getText();

        String query = "SELECT name FROM product ORDER BY id WHERE name LIKE '%?%';";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, text);
            ResultSet rs = pstmt.executeQuery();
            
            int i = 0;
            while (rs.next() && i < 30) {
                menuButtons.get(i).setText(rs.getString("name"));
                i++;
            }
            for ( ; i < 30; i++) {
                menuButtons.get(i).setVisible(false);;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderInfo() {
        String info = "";
        float total = 0;

        String query = "SELECT snapshot_name, sold_price FROM \"order_detail\" WHERE id = ?;";

        for (Integer detailID : detailIDs) {
            try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, detailID);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    info += rs.getString("snapshot_name") + "\n";
                    total += rs.getFloat("sold_price");
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        orderInfo.setText(info);
        float tax = total * (float)0.0825;

        query = "UPDATE \"order\" SET total_tax = ?, total_final = ? WHERE id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setFloat(1, total);
            pstmt.setFloat(2, total + tax);
            pstmt.setInt(3, orderID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        orderTotal.setText("Subtotal: " + String.valueOf(total) + 
                     "\nTax: " + String.valueOf(tax) +
                     "\n----------------------" +
                     "\nTotal: " + String.valueOf(total + tax));
    }

    private void handleLogin() {
        String pin = currentPin.toString();
        if (pin.isEmpty()) {
            return;
        }

        boolean isValid = verifyPin(pin);
        
        if (isValid) {
            System.out.println("Login successful!");
            // TODO: Load the next scene (e.g., POS terminal)
            
            currentPin.setLength(0);
        } else {
            currentPin.setLength(0);
        }
    }

    private boolean verifyPin(String pin) {
        String query = "SELECT * FROM employee WHERE pin_hash = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, pin);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
