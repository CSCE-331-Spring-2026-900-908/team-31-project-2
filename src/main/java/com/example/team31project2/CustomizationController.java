package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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

        /**
         * @author Noah Higgins
         * @param o the Object/Modifier to be compared
         * @return whether or not the two Modifiers have the same id
         */
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

    @FXML
    private GridPane addOnsTable;

    @FXML
    private ToggleButton wholeMilk;
    @FXML
    private ToggleButton almondMilk;
    @FXML
    private ToggleButton oatMilk;
    @FXML
    private ToggleButton soyMilk;
    @FXML
    private ToggleButton medium;
    @FXML
    private ToggleButton large;
    
    private int orderDetailID = 1;
    private List<Modifier> modifiers;
    private Scene parentScene;
    private OrderController parentController;

    /**
     * @author Noah Higgins
     * @param scene         the scene of the order controller
     * @param controller    the reference to the parent order controller
     * @param detailID      the ID of the item to be customized
     */
    public void setParent(Scene scene, OrderController controller, int detailID) {
        parentScene = scene;
        parentController = controller;
        orderDetailID = detailID;
        setModifiers();
    }

    /**
     * @author Noah Higgins
     */
    @FXML
    public void initialize() {
    }
    
    /**
     * @author Noah Higgins
     */
    public void setModifiers() {
        modifiers = new ArrayList<Modifier>();

        String query = "SELECT modifier_option_id, price_charged, snapshot_name FROM ordermodifier WHERE order_detail_id = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, orderDetailID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                System.out.println(rs.getString("snapshot_name"));
                modifiers.add(new Modifier(rs.getInt("modifier_option_id"), rs.getString("snapshot_name"), rs.getFloat("price_charged")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "SELECT option_id, price_adjustment, name FROM modifieroption WHERE category = 'Topping'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            addOnsTable.getChildren().clear();
            int i = 0;
            while (rs.next()) {
                ToggleButton newButton = new ToggleButton(rs.getString("name"));
                // newButton.setId("modifierid" + rs.getString("option_id") + "price" + rs.getString("price_charged"));
                newButton.setStyle("-fx-pref-height: 100; -fx-pref-width: 100; -fx-font-size: 18; -fx-wrap-text: true; -fx-text-alignment: center");
                newButton.setOnAction(e -> toggleModifier(e));
                int row = i % 2;
                int col = i / 2;
                addOnsTable.add(newButton, col, row);
                i++;
                if (modifiers.contains(new Modifier(rs.getInt("option_id"), "", 0))) {
                    newButton.setSelected(true);
                } else {
                    newButton.setSelected(false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (modifiers.contains(new Modifier(19, "", 0))) {
            medium.setSelected(true);
        } else {
            large.setSelected(true);
        }
        
        if (modifiers.contains(new Modifier(21, "", 0))) {
            wholeMilk.setSelected(true);
        } else if (modifiers.contains(new Modifier(22, "", 0))) {
            almondMilk.setSelected(true);
        } else if (modifiers.contains(new Modifier(23, "", 0))) {
            oatMilk.setSelected(true);
        } else if (modifiers.contains(new Modifier(24, "", 0))) {
            soyMilk.setSelected(true);
        }
        
        if (modifiers.contains(new Modifier(10, "", 0))) {
            sweetnessLevel.setValue(100);
        } else if (modifiers.contains(new Modifier(11, "", 0))) {
            sweetnessLevel.setValue(75);
        } else if (modifiers.contains(new Modifier(12, "", 0))) {
            sweetnessLevel.setValue(50);
        } else if (modifiers.contains(new Modifier(13, "", 0))) {
            sweetnessLevel.setValue(25);
        } else if (modifiers.contains(new Modifier(14, "", 0))) {
            sweetnessLevel.setValue(0);
        }
        
        if (modifiers.contains(new Modifier(15, "", 0))) {
            iceLevel.setValue(15);
        } else if (modifiers.contains(new Modifier(16, "", 0))) {
            iceLevel.setValue(16);
        } else if (modifiers.contains(new Modifier(17, "", 0))) {
            iceLevel.setValue(17);
        } else if (modifiers.contains(new Modifier(18, "", 0))) {
            iceLevel.setValue(18);
        }
    }

    /**
     * @author Noah Higgins
     * @param event the ToggleButton being pressed, with text that is the name of the topping to be toggled
     */
    @FXML
    void toggleModifier(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        String text = source.getText();

        String query = "SELECT option_id, price_adjustment FROM modifieroption WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, text);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Modifier modifier = new Modifier(rs.getInt("option_id"), text, rs.getFloat("price_adjustment"));
                if (modifiers.contains(modifier)) {
                    System.out.println("Removing " + text + "...");
                    modifiers.remove(modifier);
                } else {
                    System.out.println("Adding " + text + "...");
                    modifiers.add(modifier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Noah Higgins
     * @param event the ToggleButton being pressed, with text that is the name of the size being selected
     */
    @FXML
    void setSize(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        String text = source.getText();

        modifiers.removeIf(modifier -> modifier.name.equals("Medium") || modifier.name.equals("Large"));
        if (text.equals("Medium")) {
            modifiers.add(new Modifier(19, text, 0));
        } else {
            modifiers.add(new Modifier(20, text, 0.75f));
        }
    }

    /**
     * @author Noah Higgins
     * @param event the ToggleButton being pressed, with text that is the name of the milk type being selected
     */
    @FXML
    void setMilk(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        String text = source.getText();

        modifiers.removeIf(modifier -> modifier.name.contains("Milk"));
        if (text.equals("Whole Milk")) {
            modifiers.add(new Modifier(21, text, 0));
        } else if (text.equals("Almond Milk")) {
            modifiers.add(new Modifier(22, text, 0.5f));
        } else if (text.equals("Oat Milk")) {
            modifiers.add(new Modifier(23, text, 0.5f));
        } else if (text.equals("Soy Milk")) {
            modifiers.add(new Modifier(24, text, 0.5f));
        }
    }

    /**
     * @author Noah Higgins
     * @param event the Button being pressed
     */
    @FXML
    void cancel(ActionEvent event) {
        exit();
    }

    /**
     * @author Noah Higgins
     * @param event the Button being pressed
     */
    @FXML
    void save(ActionEvent event) {
        String query = "DELETE FROM ordermodifier WHERE order_detail_id = ?;";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, orderDetailID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        modifiers.removeIf(modifier -> modifier.name.contains("Ice") || modifier.name.contains("Sugar"));
        int iceID = (int) iceLevel.getValue();
        int sweetnessID = (int) (100 - sweetnessLevel.getValue()) / 25 + 10;

        query = "SELECT name, price_adjustment FROM modifieroption WHERE option_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, iceID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                modifiers.add(new Modifier(iceID, rs.getString("name"), rs.getFloat("price_adjustment")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, sweetnessID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                modifiers.add(new Modifier(sweetnessID, rs.getString("name"), rs.getFloat("price_adjustment")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(Modifier modifier : modifiers) {
            System.out.println(modifier.name);

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

    /**
     * @author Noah Higgins
     */
    private void exit() {
        parentController.updateOrderInfo();
        Stage stage = (Stage) notes.getScene().getWindow();
        stage.setScene(parentScene);
        stage.centerOnScreen();
    }
}
