package com.example.team31project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import org.kordamp.bootstrapfx.BootstrapFX;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderController {
    private Employee currentUser;

    @FXML
    private TextArea orderInfo;

    @FXML
    private VBox orderInfoList;

    @FXML
    private TextArea orderTotal;

    @FXML
    private TextField searchBar;

    @FXML
    private MenuBar topMenuBar;

    @FXML
    private Menu navigateMenu;

    @FXML
    private Label headerUserLabel;

    @FXML private ToggleButton milkFoam;
    @FXML private ToggleButton milkTea;
    @FXML private ToggleButton creativeMix;
    @FXML private ToggleButton brewedTea;
    @FXML private ToggleButton coffee;
    @FXML private ToggleButton slush;

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
    public List<Integer> detailIDs = new ArrayList<Integer>();
    private List<Button> menuButtons;
    private List<String> category = new ArrayList<String>();
    private StringBuilder currentPin = new StringBuilder();

    private Map<String, Integer> productNameToIdMap = new HashMap<>();
    private Map<String, Float> productNameToPriceMap = new HashMap<>();

    private static class CachedModifier {
        int optionId;
        float priceAdjustment;
        String name;

        CachedModifier(int optionId, float priceAdjustment, String name) {
            this.optionId = optionId;
            this.priceAdjustment = priceAdjustment;
            this.name = name;
        }
    }

    private List<CachedModifier> defaultModifiersCache = new ArrayList<>();

    public void setUser(Employee user) {
        if (user != null) {
            this.currentUser = user;
            this.employeeID = user.getId();
            UserSession.setCurrentUser(user);
        } else {
            Employee sessionUser = UserSession.getCurrentUser();
            if (sessionUser != null) {
                this.currentUser = sessionUser;
                this.employeeID = sessionUser.getId();
            }
        }
        updateRoleVisibility();
        updateHeaderUserLabel();
    }

    @FXML
    public void initialize() {
        Employee sessionUser = UserSession.getCurrentUser();
        if (currentUser == null && sessionUser != null) {
            currentUser = sessionUser;
            employeeID = sessionUser.getId();
        }

        menuButtons = List.of(menuItem01, menuItem02, menuItem03, menuItem04, menuItem05, menuItem06,
                menuItem07, menuItem08, menuItem09, menuItem10, menuItem11, menuItem12,
                menuItem13, menuItem14, menuItem15, menuItem16, menuItem17, menuItem18,
                menuItem19, menuItem20, menuItem21, menuItem22, menuItem23, menuItem24,
                menuItem25, menuItem26, menuItem27, menuItem28, menuItem29, menuItem30);

        for (Button b : menuButtons) {
            b.managedProperty().bind(b.visibleProperty());
        }

        milkFoam.setStyle(getCategoryStyle("Milk Foam Series", false));
        milkTea.setStyle(getCategoryStyle("Milk Tea Series", false));
        creativeMix.setStyle(getCategoryStyle("Creative Mix Series", false));
        brewedTea.setStyle(getCategoryStyle("Brewed Tea Series", false));
        coffee.setStyle(getCategoryStyle("Coffee Series", false));
        slush.setStyle(getCategoryStyle("Slush Series", false));

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

        // Load product cache
        String productQuery = "SELECT product_id, base_price, name FROM product;";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(productQuery);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                productNameToIdMap.put(rs.getString("name"), rs.getInt("product_id"));
                productNameToPriceMap.put(rs.getString("name"), rs.getFloat("base_price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load default modifiers cache
        String modifierQuery = "SELECT option_id, price_adjustment, name FROM modifieroption WHERE is_default = true;";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(modifierQuery);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                defaultModifiersCache.add(new CachedModifier(
                        rs.getInt("option_id"),
                        rs.getFloat("price_adjustment"),
                        rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        filter();
        // query = "SELECT name FROM product ORDER BY product_id;";

        // try (Connection conn = DatabaseConnection.getConnection();
        //      PreparedStatement pstmt = conn.prepareStatement(query)) {

        //     ResultSet rs = pstmt.executeQuery();

        //     int i = 0;
        //     while (rs.next() && i < 30) {
        //         menuButtons.get(i).setText(rs.getString("name"));
        //         menuButtons.get(i).setVisible(true);
        //         i++;
        //     }
        //     for ( ; i < 30; i++) {
        //         menuButtons.get(i).setVisible(false);
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }

        updateRoleVisibility();
        updateHeaderUserLabel();
    }

    private void updateRoleVisibility() {
        if (navigateMenu == null) {
            return;
        }

        boolean canSeeNavigate = currentUser != null && currentUser.isManager();
        navigateMenu.setVisible(canSeeNavigate);

        if (topMenuBar != null) {
            topMenuBar.setVisible(canSeeNavigate);
            topMenuBar.setManaged(canSeeNavigate);
        }
    }

    private void updateHeaderUserLabel() {
        if (headerUserLabel == null) {
            return;
        }

        if (currentUser == null) {
            headerUserLabel.setText("Cashier");
            return;
        }

        headerUserLabel.setText(currentUser.getName() + " (" + currentUser.getRole() + ")");
    }

    @FXML
    void toggleCategory(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        String text = source.getText();

        if(category.contains(text)) {
            category.remove(text);
        } else {
            category.add(text);
        }
        source.setStyle(getCategoryStyle(text, source.isSelected()));
        filter();
        // if (category.contains(text)) {
        //     category = category.substring(0, category.indexOf(text)) + category.substring(category.indexOf(text) + text.length());
        // } else {
        //     category += text;
        // }
    }

    @FXML
    void addDrink(ActionEvent event) {
        Button source = (Button) event.getSource();
        String productName = source.getText();

        if (!productNameToIdMap.containsKey(productName)) {
            System.out.println("Product not found in cache: " + productName);
            return;
        }

        int productID = productNameToIdMap.get(productName);
        float productPrice = productNameToPriceMap.get(productName);

        String query = "INSERT INTO \"orderdetail\" (order_id, product_id, sold_price, snapshot_name) VALUES (?, ?, ?, ?) RETURNING id;";
        int newDetailID = 1;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderID);
            pstmt.setInt(2, productID);
            pstmt.setFloat(3, productPrice);
            pstmt.setString(4, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                newDetailID = rs.getInt("id");
                detailIDs.add(newDetailID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Batch insert default modifiers
        if (!defaultModifiersCache.isEmpty()) {
            String modQuery = "INSERT INTO \"ordermodifier\" (order_detail_id, modifier_option_id, price_charged, snapshot_name) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(modQuery)) {

                for (CachedModifier mod : defaultModifiersCache) {
                    pstmt.setInt(1, newDetailID);
                    pstmt.setInt(2, mod.optionId);
                    pstmt.setFloat(3, mod.priceAdjustment);
                    pstmt.setString(4, mod.name);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        updateOrderInfo();

        try {
            customize(newDetailID, productID, true);
        } catch (IOException e) {
            System.out.println("Failed to customize");
        }
    }

    @FXML
    void search(KeyEvent event) {
        filter();
    }

    @FXML
    void handleNavigateMenuEdit(ActionEvent event) {
        navigateTo("menu-edit-view.fxml");
    }

    @FXML
    void handleNavigateEmployees(ActionEvent event) {
        navigateTo("employee-list-view.fxml");
    }

    @FXML
    void handleNavigateInventory(ActionEvent event) {
        navigateTo("inventory-view.fxml");
    }

    @FXML
    void handleNavigateTrends(ActionEvent event) {
        navigateTo("reports-view.fxml");
    }

    @FXML
    void handleSignOut(ActionEvent event) {
        UserSession.clear();
        navigateTo("login-view.fxml");
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) orderInfo.getScene().getWindow();
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

    private String getCategoryStyle(String category, boolean isSelected) {
        String color;
        switch (category) {
            case "Milk Foam Series":
                color = "#E0F7FA"; // Light Cyan
                break;
            case "Milk Tea Series":
                color = "#FFF8E1"; // Light amber/cream
                break;
            case "Creative Mix Series":
                color = "#F3E5F5"; // Light Purple
                break;
            case "Brewed Tea Series":
                color = "#E8F5E9"; // Light Green
                break;
            case "Coffee Series":
                color = "#EFEBE9"; // Light Brown
                break;
            case "Slush Series":
                color = "#FFEBEE"; // Light Red/Pink
                break;
            default:
                color = "white";
                break;
        }
        String borderStyle = isSelected ? "-fx-border-color: #555555; -fx-border-width: 3;"
                : "-fx-border-color: #bdbdbd; -fx-border-width: 1;";
        return borderStyle + " -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: " + color + ";";
    }

    private void filter() {
        String query = "SELECT name, category_name FROM product WHERE name ILIKE ? ORDER BY product_id;";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchBar.getText() + "%");
            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            while (rs.next() && i < 30) {
                if (category.size() == 0 || category.contains(rs.getString("category_name"))) {
                    menuButtons.get(i).setText(rs.getString("name"));
                    menuButtons.get(i).setStyle(getCategoryStyle(rs.getString("category_name"), false));
                    menuButtons.get(i).setVisible(true);
                    i++;
                }
            }
            for (; i < 30; i++) {
                menuButtons.get(i).setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void checkout(ActionEvent event) {
        detailIDs = new ArrayList<Integer>();
        // try (Connection conn = DBConnection.getConnection()) {

        //     }
        // for(int detailID : detailIDs){
        //     String query = "UPDATE inventory SET quantity = quantity - 1 WHERE id = ?"; 

        //     PreparedStatement ps = 
        // }
        initialize();
        updateOrderInfo();
    }

    public void updateOrderInfo() {
        orderInfoList.getChildren().clear();
        float subtotal = 0;

        // Optimized query to fetch all details and modifiers for the current order in
        // ONE go
        String query = "SELECT od.id AS detail_id, od.product_id AS product_id, od.snapshot_name AS prod_name, od.sold_price, "
                +
                "om.snapshot_name AS mod_name, om.price_charged AS mod_price " +
                "FROM orderdetail od " +
                "LEFT JOIN ordermodifier om ON od.id = om.order_detail_id " +
                "WHERE od.order_id = ? " +
                "ORDER BY od.id, om.id";

        // Map to store item info since result set has one row per modifier
        // Key: detailID, Value: [Text Builder, Total Price including mods, Product ID]
        java.util.LinkedHashMap<Integer, StringBuilder> itemTexts = new java.util.LinkedHashMap<>();
        java.util.LinkedHashMap<Integer, Float> itemPrices = new java.util.LinkedHashMap<>();
        java.util.LinkedHashMap<Integer, Integer> itemProductIds = new java.util.LinkedHashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int detailId = rs.getInt("detail_id");
                String prodName = rs.getString("prod_name");
                float prodPrice = rs.getFloat("sold_price");
                String modName = rs.getString("mod_name");
                float modPrice = rs.getFloat("mod_price"); // 0.0 if null usually, but check logic

                if (!itemTexts.containsKey(detailId)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" - ").append(prodName != null ? prodName : "Unknown").append("\n");
                    itemTexts.put(detailId, sb);
                    itemPrices.put(detailId, prodPrice);
                    itemProductIds.put(detailId, rs.getInt("product_id"));
                }

                // If there is a modifier (mod_name is not null)
                if (modName != null) {
                    itemTexts.get(detailId).append("   - ").append(modName).append("\n");
                    itemPrices.put(detailId, itemPrices.get(detailId) + modPrice);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Reconstruct the buttons in order
        for (java.util.Map.Entry<Integer, StringBuilder> entry : itemTexts.entrySet()) {
            Integer detailID = entry.getKey();
            String text = entry.getValue().toString();
            Float price = itemPrices.get(detailID);
            Integer prodId = itemProductIds.get(detailID);
            subtotal += price;

            Button newButton = new Button(text);
            newButton.setMaxWidth(Double.MAX_VALUE); // Fill horizontally
            newButton.getStyleClass().add("order-item-button"); // Apply CSS class
            HBox.setHgrow(newButton, Priority.ALWAYS); // Grow flexibly inside HBox

            // Store product_id in UserData to retrieve it during customization
            newButton.setUserData(prodId);
            newButton.setOnAction(e -> customizeItem(e));
            newButton.setId("item" + detailID.toString());

            Button deleteBtn = new Button("X");
            deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold; ");
            deleteBtn.setPrefHeight(newButton.getPrefHeight()); // Try to match height
            deleteBtn.setMaxHeight(Double.MAX_VALUE);
            deleteBtn.setOnAction(e -> deleteItem(detailID));

            HBox row = new HBox(5, newButton, deleteBtn);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setMaxWidth(Double.MAX_VALUE);

            orderInfoList.getChildren().add(row);
        }

        float tax = Math.round(subtotal * 0.0825f * 100.0f) / 100.0f;
        float totalFinal = subtotal + tax;

        // Update totals text area
        orderTotal.setText(String.format("$%.2f\n$%.2f\n-----\n$%.2f", subtotal, tax, totalFinal));

        String updateQuery = "UPDATE \"order\" SET total_tax = ?, total_final = ? WHERE id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setFloat(1, tax);
            pstmt.setFloat(2, totalFinal);
            pstmt.setInt(3, orderID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(int detailID) {
        String deleteModifiersQuery = "DELETE FROM ordermodifier WHERE order_detail_id = ?;";
        String deleteDetailQuery = "DELETE FROM orderdetail WHERE id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement modPstmt = conn.prepareStatement(deleteModifiersQuery);
                PreparedStatement detailPstmt = conn.prepareStatement(deleteDetailQuery)) {

            // Delete modifiers first (foreign key constraint)
            modPstmt.setInt(1, detailID);
            modPstmt.executeUpdate();

            // Delete the main detail row
            detailPstmt.setInt(1, detailID);
            detailPstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Repaint the UI and recalculate the totals
        updateOrderInfo();
    }

    @FXML
    void customizeItem(ActionEvent event) {
        Button source = (Button) event.getSource();
        int detailID = Integer.valueOf(source.getId().substring(4));
        int productID = -1;
        if (source.getUserData() != null) {
            productID = (int) source.getUserData();
        }

        try {
            customize(detailID, productID, false);
        } catch (IOException e) {
            System.out.println("Failed to customize");
        }
    }

    private void customize(int detailID, int productID, boolean isNew) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customization-view.fxml"));
            Parent root = loader.load();
            CustomizationController customizer = loader.getController();
            Stage stage = (Stage) orderInfo.getScene().getWindow();
            customizer.setParent(stage.getScene(), this, detailID, productID, isNew);
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
