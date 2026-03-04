/*
@author Team-31
*/
package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
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
   
    static class Modifier {
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
     /* 
        @param takes an action 
        @returns nothing
    */
    void decreaseQuantity(ActionEvent event) {
        try {
            int currentQty = Integer.parseInt(quantity.getText().trim());
            if (currentQty > 1) {
                quantity.setText(String.valueOf(currentQty - 1));
            }
        } catch (NumberFormatException e) {
            quantity.setText("1");
        }
    }

    @FXML
    /* 
        @param takes an action 
        @returns nothing
    */
    void increaseQuantity(ActionEvent event) {
        try {
            int currentQty = Integer.parseInt(quantity.getText().trim());
            quantity.setText(String.valueOf(currentQty + 1));
        } catch (NumberFormatException e) {
            quantity.setText("1");
        }
    }

    @FXML
    private Slider iceLevel;

    @FXML
    private Slider sweetnessLevel;

    @FXML
    private FlowPane addOnsTable;

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
    private int productID = -1;
    private boolean isNew = false;
    private List<Modifier> modifiers;
    private Scene parentScene;
    private OrderController parentController;
   
    // A list of valid modifier options for this specific product
    private List<Integer> validModifierIds = new ArrayList<>();

        /* 
        @param scene: takes the current scene 
        @param controller: takes the controller of that sence 
        @param detailID: gets the ID of that order detial like a modifer 
        @param prodID: gets the product id 
        @returns nothing
        */  
    public void setParent(Scene scene, OrderController controller, int detailID, int prodID, boolean isNewItem) {
        parentScene = scene;
        parentController = controller;
        orderDetailID = detailID;
        productID = prodID;
        isNew = isNewItem;
        setModifiers();
    }

    @FXML
    public void initialize() {
    }

    private static List<Modifier> cachedToppings = null;

    
        /* 
        @returns nothing
        */  
    private void loadToppingsCache() {
        if (cachedToppings != null)
            return;
        cachedToppings = new ArrayList<>();
        String query = "SELECT option_id, price_adjustment, name FROM modifieroption WHERE category = 'Topping'";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                cachedToppings.add(new Modifier(
                        rs.getInt("option_id"),
                        rs.getString("name"),
                        rs.getFloat("price_adjustment")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String[] ICE_NAMES = {
            "No Ice", "Less Ice", "Regular Ice", "More Ice", "Extra Ice"
    };
    private static final float[] ICE_PRICES = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };

    private static final String[] SWEET_NAMES = {
            "100% Sugar", "75% Sugar", "50% Sugar", "25% Sugar", "0% Sugar"
    };
    private static final float[] SWEET_PRICES = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };

    /* 
        @returns nothing
    */  
    public void setModifiers() {
        modifiers = new ArrayList<Modifier>();
        validModifierIds.clear();

        // 1. Fetch valid modifier IDs for this product from ProductModifier
        if (productID != -1) {
            String prodQuery = "SELECT option_id FROM productmodifier WHERE product_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(prodQuery)) {
                pstmt.setInt(1, productID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    validModifierIds.add(rs.getInt("option_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 2. Load existing modifiers for this order detail (and notes)
        String query = "SELECT modifier_option_id, price_charged, snapshot_name FROM ordermodifier WHERE order_detail_id = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderDetailID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("snapshot_name"));
                modifiers.add(new Modifier(rs.getInt("modifier_option_id"), rs.getString("snapshot_name"),
                        rs.getFloat("price_charged")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load notes from the DB
        String notesQuery = "SELECT notes FROM orderdetail WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(notesQuery)) {
            pstmt.setInt(1, orderDetailID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String savedNotes = rs.getString("notes");
                if (savedNotes != null) {
                    notes.setText(savedNotes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3. Populate Toppings dynamically
        loadToppingsCache();
        addOnsTable.getChildren().clear();

        for (Modifier option : cachedToppings) {
            int optionId = option.id;
            // Only create buttons for toppings valid for this product
            if (!validModifierIds.contains(optionId) && !validModifierIds.isEmpty()) {
                continue;
            }

            ToggleButton newButton = new ToggleButton(option.name);
            newButton.setStyle(
                    "-fx-pref-height: 100; -fx-pref-width: 150; -fx-font-size: 18; -fx-wrap-text: true; -fx-text-alignment: center");
            newButton.setUserData(option);
            newButton.setOnAction(e -> toggleModifier(e));
            addOnsTable.getChildren().add(newButton);

            if (modifiers.contains(new Modifier(optionId, "", 0))) {
                newButton.setSelected(true);
            } else {
                newButton.setSelected(false);
            }
        }

        // 4. Disable UI elements if their modifier ID is not valid for this product
        if (!validModifierIds.isEmpty()) {
            medium.setDisable(!validModifierIds.contains(19));
            large.setDisable(!validModifierIds.contains(20));

            wholeMilk.setDisable(!validModifierIds.contains(21));
            almondMilk.setDisable(!validModifierIds.contains(22));
            oatMilk.setDisable(!validModifierIds.contains(23));
            soyMilk.setDisable(!validModifierIds.contains(24));
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

    /*  
        @param docuemnts events 
        @returns nothing
    */  
    @FXML
    void toggleModifier(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        Modifier modifier = (Modifier) source.getUserData();

        if (modifier == null)
            return;

        if (modifiers.contains(modifier)) {
            System.out.println("Removing " + modifier.name + "...");
            modifiers.remove(modifier);
        } else {
            System.out.println("Adding " + modifier.name + "...");
            modifiers.add(modifier);
        }
    }

    @FXML
    /*  
        @param gets events 
        @returns nothing
    */  
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

    @FXML
    /*  
        @param gets events 
        @returns nothing
    */  
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
        // String query = "SELECT option_id, price_adjustment FROM modifieroption WHERE name = ?";

    // try (Connection conn = DatabaseConnection.getConnection();
        //      PreparedStatement pstmt = conn.prepareStatement(query)) {

        //     pstmt.setString(1, text);
        //     ResultSet rs = pstmt.executeQuery();

        //     if (rs.next()) {
        //         Modifier modifier = new Modifier(rs.getInt("option_id"), text, rs.getFloat("price_adjustment"));
        //         if (modifiers.contains(modifier)) {
        //             System.out.println("Removing " + text + "...");
        //             modifiers.remove(modifier);
        //         } else {
        //             System.out.println("Adding " + text + "...");
        //             modifiers.add(modifier);
        //         }
        //     }
    // } catch (SQLException e) {
        //     e.printStackTrace();
    // }

    @FXML
    /*  
        @param gets events 
        @returns nothing
    */  
    void cancel(ActionEvent event) {
        if (isNew) {
            // Clean up the newly-created order detail since the user cancelled
            parentController.deleteItem(orderDetailID);
        }
        exit();
    }

    @FXML
    /*  
        @param gets events 
        @returns nothing
    */  
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

        int iceID = (int) iceLevel.getValue(); // e.g., 15 to 18
        if (iceID >= 15 && iceID <= 18) {
            modifiers.add(new Modifier(iceID, ICE_NAMES[iceID - 15], ICE_PRICES[iceID - 15]));
        }

        int sweetnessPercent = (int) sweetnessLevel.getValue();
        int sweetnessID = (100 - sweetnessPercent) / 25 + 10; // e.g., 10 to 14
        if (sweetnessID >= 10 && sweetnessID <= 14) {
            modifiers.add(new Modifier(sweetnessID, SWEET_NAMES[sweetnessID - 10], SWEET_PRICES[sweetnessID - 10]));
        }

        String insertModQuery = "INSERT INTO \"ordermodifier\" (order_detail_id, modifier_option_id, price_charged, snapshot_name) VALUES (?, ?, ?, ?) RETURNING id;";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(insertModQuery)) {

            for (Modifier modifier : modifiers) {
                pstmt.setInt(1, orderDetailID);
                pstmt.setInt(2, modifier.id);
                pstmt.setFloat(3, modifier.price);
                pstmt.setString(4, modifier.name);
                pstmt.addBatch();
            }
            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Persist notes
        String orderNotes = notes.getText();
        String updateNotesQuery = "UPDATE orderdetail SET notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(updateNotesQuery)) {
            pstmt.setString(1, orderNotes.isBlank() ? null : orderNotes);
            pstmt.setInt(2, orderDetailID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Handle Quantity > 1: Clone the order details and modifiers
        int qty = 1;
        try {
            qty = Integer.parseInt(quantity.getText().trim());
        } catch (NumberFormatException e) {
            qty = 1;
        }

        if (qty > 1) {
            // First get the order_id, product_id, sold_price, and snapshot_name from the
            // current order detail
            int orderId = -1;
            float soldPrice = 0.0f;
            String snapshotName = "";
            String detailQuery = "SELECT order_id, sold_price, snapshot_name FROM orderdetail WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(detailQuery)) {
                pstmt.setInt(1, orderDetailID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    orderId = rs.getInt("order_id");
                    soldPrice = rs.getFloat("sold_price");
                    snapshotName = rs.getString("snapshot_name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (orderId != -1) {
                List<Integer> newDetailIDs = new ArrayList<>();
                // Create new OrderDetails in batch or sequentially depending on RETURNING needs
                String insertDetailQuery = "INSERT INTO \"orderdetail\" (order_id, product_id, sold_price, snapshot_name) VALUES (?, ?, ?, ?) RETURNING id;";
                try (Connection conn = DatabaseConnection.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(insertDetailQuery)) {

                    for (int i = 1; i < qty; i++) {
                        pstmt.setInt(1, orderId);
                        pstmt.setInt(2, productID);
                        pstmt.setFloat(3, soldPrice);
                        pstmt.setString(4, snapshotName);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            newDetailIDs.add(rs.getInt("id"));
                            parentController.detailIDs.add(rs.getInt("id"));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Batch copy all modifiers for all new details
                if (!newDetailIDs.isEmpty()) {
                    String batchModQuery = "INSERT INTO \"ordermodifier\" (order_detail_id, modifier_option_id, price_charged, snapshot_name) VALUES (?, ?, ?, ?)";
                    try (Connection conn = DatabaseConnection.getConnection();
                            PreparedStatement pstmt = conn.prepareStatement(batchModQuery)) {

                        for (int newId : newDetailIDs) {
                            for (Modifier modifier : modifiers) {
                                pstmt.setInt(1, newId);
                                pstmt.setInt(2, modifier.id);
                                pstmt.setFloat(3, modifier.price);
                                pstmt.setString(4, modifier.name);
                                pstmt.addBatch();
                            }
                        }
                        pstmt.executeBatch();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Propagate notes to cloned entries
                    if (!orderNotes.isBlank()) {
                        String batchNotesQuery = "UPDATE orderdetail SET notes = ? WHERE id = ?";
                        try (Connection conn = DatabaseConnection.getConnection();
                                PreparedStatement pstmt = conn.prepareStatement(batchNotesQuery)) {
                            for (int newId : newDetailIDs) {
                                pstmt.setString(1, orderNotes);
                                pstmt.setInt(2, newId);
                                pstmt.addBatch();
                            }
                            pstmt.executeBatch();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        exit();
    }

    /*  
        @returns nothing
    */  
    private void exit() {
        parentController.updateOrderInfo();
        Stage stage = (Stage) notes.getScene().getWindow();
        stage.setScene(parentScene);
        stage.centerOnScreen();
    }

    // private void showError(String message) {
    //     errorLabel.setText(message);
    //     errorLabel.setVisible(true);
    // }
}
