package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.List;

import org.controlsfx.control.action.Action;

import java.util.ArrayList;

public class CustomizationController {
    class Modifier {
        public int id;
        public String name;
        public float price;

        public Modifier(int id, String name, float price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Modifier)) {
                return false;
            }
            Modifier other = (Modifier) o;
            return this.id == other.id;
        }
    }

    @FXML
    private TextArea notes;

    @FXML
    private TextField quantity;

    @FXML
    private Slider iceLevel;

    @FXML
    private Slider sweetnessLevel;

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
    
    private int orderDetailID = 1;
    private List<Button> menuButtons;
    private List<Modifier> modifiers;
    private Scene parentScene;

    public void setParentScene(Scene parent) {
        parentScene = parent;
    }

    @FXML
    public void initialize() {
        menuButtons = List.of(menuItem01, menuItem02, menuItem03, menuItem04, menuItem05, menuItem06,
                              menuItem07, menuItem08, menuItem09, menuItem10, menuItem11, menuItem12,
                              menuItem13, menuItem14, menuItem15, menuItem16, menuItem17, menuItem18,
                              menuItem19, menuItem20, menuItem21, menuItem22, menuItem23, menuItem24,
                              menuItem25, menuItem26, menuItem27, menuItem28, menuItem29, menuItem30);
        
        modifiers = new ArrayList<Modifier>();

        String query = "SELECT modifier_option_id, price_charged, snapshot_name FROM ordermodifier WHERE order_detail_id = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, orderDetailID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                modifiers.add(new Modifier(rs.getInt("modifier_option_id"), rs.getString("snapshot_name"), rs.getFloat("price_charged")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "SELECT option_id, price_charged, name FROM modifieroption WHERE category = 'topping'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ToggleButton newButton = new ToggleButton(rs.getString("name"));
                // newButton.setId("modifierid" + rs.getString("option_id") + "price" + rs.getString("price_charged"));
                newButton.setStyle("height: 100; width: 100;");
                if (modifiers.contains(new Modifier(rs.getInt("modifier_option_id"), "", 0))) {
                    newButton.setSelected(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toggleModifier(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        String text = source.getText();

        String query = "SELECT option_id, price_charged FROM modifieroption WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, text);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Modifier modifier = new Modifier(rs.getInt("option_id"), text, rs.getFloat("price_charged"));
                if (modifiers.contains(modifier)) {
                    modifiers.remove(modifier);
                } else {
                    modifiers.add(modifier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // String fxid = source.getId();
        // int id = Integer.valueOf(source.getId().substring(8));
        // if (modifiers.contains(id)) {
        //     modifiers.remove(id);
        // } else {
        //     modifiers.add(id);
        // }
    }

    // @FXML
    // void setIce(KeyEvent event) {
    //     double level = iceLevel.getValue();
    //     int id = (int)level / 25 + 15;
        
    //     String query = "SELECT name, price_charged FROM modifieroption WHERE option_id = ?";

    //     try (Connection conn = DatabaseConnection.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(query)) {
            
    //         pstmt.setInt(1, id);
    //         ResultSet rs = pstmt.executeQuery();
            
    //         if (rs.next()) {
    //             Modifier modifier = new Modifier(id, rs.getString("name"), rs.getFloat("price_charged"))
    //             if (modifiers.contains(modifier)) {
    //                 modifiers.remove(modifier);
    //             } else {
    //                 modifiers.add(modifier);
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    // @FXML
    // void setSweetness(KeyEvent event) {
    //     double level = sweetnessLevel.getValue();
    //     int id = (int)level / 25 + 10;
        
    //     String query = "SELECT name, price_charged FROM modifieroption WHERE option_id = ?";

    //     try (Connection conn = DatabaseConnection.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(query)) {
            
    //         pstmt.setInt(1, id);
    //         ResultSet rs = pstmt.executeQuery();
            
    //         if (rs.next()) {
    //             Modifier modifier = new Modifier(id, rs.getString("name"), rs.getFloat("price_charged"))
    //             if (modifiers.contains(modifier)) {
    //                 modifiers.remove(modifier);
    //             } else {
    //                 modifiers.add(modifier);
    //             }
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    @FXML
    void cancel(ActionEvent event) {
        exit();
    }

    @FXML
    void save(ActionEvent event) {
        String query = "DELETE * FROM ordermodifier WHERE order_detail_id = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, orderDetailID);
            pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        modifiers.removeIf(modifier -> modifier.name.contains("Ice") || modifier.name.contains("Sugar"));
        int iceID = (int) iceLevel.getValue() / 25 + 15;
        int sweetnessID = (int) sweetnessLevel.getValue() / 25 + 10;

        query = "SELECT name, price_charged FROM modifieroption WHERE option_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, iceID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                modifiers.add(new Modifier(iceID, rs.getString("name"), rs.getFloat("price_charged")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, sweetnessID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                modifiers.add(new Modifier(iceID, rs.getString("name"), rs.getFloat("price_charged")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(Modifier modifier : modifiers) {
            
            query = "INSERT INTO \"ordermodifier\" (order_detail_id, modifier_option_id, price_charged, snapshot_name) VALUES (?, ?, ?, ?) RETURNING id;";
        
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
            
                pstmt.setInt(1, orderDetailID);
                pstmt.setInt(2, modifier.id);
                pstmt.setFloat(3, modifier.price);
                pstmt.setString(4, modifier.name);
                pstmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        exit();
    }

    private void exit() {
        Stage stage = (Stage) notes.getScene().getWindow();
        stage.setScene(parentScene);
        stage.centerOnScreen();
    }
    
    // private void showError(String message) {
    //     errorLabel.setText(message);
    //     errorLabel.setVisible(true);
    // }
}
