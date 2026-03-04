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
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
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

public class MenuEditController {

    @FXML
    private TextField searchField;
    @FXML
    private VBox menuItemsList;

    private final List<HBox> allRows = new ArrayList<>();

    @FXML
    public void initialize() {
        loadMenuItems();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterRows(newVal));
    }

    private void loadMenuItems() {
        String query = "SELECT product_id, name, base_price, category_name FROM product ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            allRows.clear();
            menuItemsList.getChildren().clear();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                double price = rs.getDouble("base_price");
                String category = rs.getString("category_name");
                HBox row = buildRow(id, name, price, category != null ? category : "");
                allRows.add(row);
                menuItemsList.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox buildRow(int id, String name, double price, String category) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-border-color: transparent transparent #e0e0e0 transparent;" +
                        "-fx-border-width: 0 0 1 0;" +
                        "-fx-padding: 10 0 10 0;");

        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(300.0);
        nameLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #333333;");

        Label categoryLabel = new Label(category);
        categoryLabel.setPrefWidth(200.0);
        categoryLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

        Label priceLabel = new Label(String.format("$%.2f", price));
        priceLabel.setPrefWidth(100.0);
        priceLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

        Button editBtn = new Button("✎");
        editBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #555555;" +
                        "-fx-border-radius: 50;" +
                        "-fx-background-radius: 50;" +
                        "-fx-padding: 4 9 4 9;" +
                        "-fx-font-size: 13;");
        editBtn.setOnAction(e -> handleEditMenuItem(id, name, price, category));

        Button removeBtn = new Button("✕");
        removeBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #cc3333;" +
                        "-fx-border-radius: 50;" +
                        "-fx-background-radius: 50;" +
                        "-fx-padding: 4 9 4 9;" +
                        "-fx-font-size: 13;" +
                        "-fx-text-fill: #cc3333;");
        removeBtn.setOnAction(e -> handleRemoveMenuItem(id, name, row));

        HBox actions = new HBox(8, editBtn, removeBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(nameLabel, categoryLabel, priceLabel, actions);
        return row;
    }

    private void filterRows(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        menuItemsList.getChildren().clear();
        for (HBox row : allRows) {
            Label nameLabel = (Label) row.getChildren().get(0);
            Label categoryLabel = (Label) row.getChildren().get(1);
            if (nameLabel.getText().toLowerCase().contains(lower)
                    || categoryLabel.getText().toLowerCase().contains(lower)) {
                menuItemsList.getChildren().add(row);
            }
        }
    }

    private void handleEditMenuItem(int id, String currentName, double currentPrice, String currentCategory) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Menu Item");
        dialog.setHeaderText("Manager Only - Edit Menu Item");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20, 20, 20, 20));
        content.setPrefWidth(700);

        // Fetch is_active
        boolean currentIsActive = true;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT is_active FROM product WHERE product_id = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    currentIsActive = rs.getBoolean("is_active");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Name & Category
        GridPane topGrid = new GridPane();
        topGrid.setHgap(20);
        topGrid.setVgap(15);

        Label nameLbl = new Label("Drink Name:");
        nameLbl.setStyle("-fx-font-weight: bold;");
        TextField nameField = new TextField(currentName);
        nameField.setPrefWidth(400);
        nameField.setStyle(
                "-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #ccc; -fx-padding: 5 10 5 10;");

        Label catLbl = new Label("Category:");
        catLbl.setStyle("-fx-font-weight: bold;");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.setPrefWidth(400);
        categoryBox.setStyle(
                "-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #ccc; -fx-background-color: white;");
        categoryBox.setEditable(true);
        if (currentCategory != null) {
            categoryBox.setValue(currentCategory);
        }

        topGrid.add(nameLbl, 0, 0);
        topGrid.add(nameField, 1, 0);
        topGrid.add(catLbl, 0, 1);
        topGrid.add(categoryBox, 1, 1);

        // Pricing
        Label priceHeader = new Label("Pricing");
        priceHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        HBox priceBox = new HBox(15);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        Label priceLbl = new Label("Base Price: $");
        TextField priceField = new TextField(String.valueOf(currentPrice));
        priceField.setPrefWidth(100);
        priceField.setStyle(
                "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ccc; -fx-padding: 5 10 5 10;");
        priceBox.getChildren().addAll(priceLbl, priceField);
        // Fetch existing modifiers
        List<Integer> existingModifiers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT option_id FROM ProductModifier WHERE product_id = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    existingModifiers.add(rs.getInt("option_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Modifiers Sections
        Label modifiersHeader = new Label("Modifiers");
        modifiersHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        content.getChildren().addAll(topGrid, priceHeader, priceBox, modifiersHeader);

        java.util.Map<Integer, ToggleButton> modifierButtons = new java.util.HashMap<>();
        java.util.Map<String, FlowPane> categoryPanes = new java.util.HashMap<>();
        List<ToggleButton> toppingButtons = new ArrayList<>();

        String[] categories = { "Topping", "Milk Type", "Sugar Level", "Ice Level", "Size" };
        for (String cat : categories) {
            Label modCatLbl = new Label(cat + ":");
            modCatLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            FlowPane pane = new FlowPane();
            pane.setHgap(10);
            pane.setVgap(10);
            categoryPanes.put(cat, pane);

            if (cat.equals("Topping")) {
                HBox catHeader = new HBox(15);
                catHeader.setAlignment(Pos.CENTER_LEFT);
                Button toggleAllBtn = new Button("Toggle All");
                toggleAllBtn.setStyle(
                        "-fx-background-color: transparent; -fx-border-color: #00bcd4; -fx-text-fill: #00bcd4; -fx-border-radius: 10; -fx-background-radius: 10;");
                toggleAllBtn.setOnAction(e -> {
                    boolean allSelected = toppingButtons.stream().allMatch(ToggleButton::isSelected);
                    for (ToggleButton tb : toppingButtons) {
                        tb.setSelected(!allSelected);
                    }
                });
                catHeader.getChildren().addAll(modCatLbl, toggleAllBtn);
                content.getChildren().addAll(catHeader, pane);
            } else {
                content.getChildren().addAll(modCatLbl, pane);
            }
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT option_id, name, category, price_adjustment FROM ModifierOption ORDER BY category, name");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int optionId = rs.getInt("option_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double priceAdj = rs.getDouble("price_adjustment");

                String btnText = name + (priceAdj > 0 ? String.format(" (+$%.2f)", priceAdj) : "");
                ToggleButton btn = new ToggleButton(btnText);
                btn.setPrefSize(120, 50);
                btn.setWrapText(true);
                btn.setStyle(
                        "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");

                if (existingModifiers.contains(optionId)) {
                    btn.setSelected(true);
                    btn.setStyle(
                            "-fx-background-color: #fff3e0; -fx-border-color: #ff9800; -fx-background-radius: 5; -fx-border-radius: 5;");
                }

                btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal) {
                        btn.setStyle(
                                "-fx-background-color: #fff3e0; -fx-border-color: #ff9800; -fx-background-radius: 5; -fx-border-radius: 5;");
                    } else {
                        btn.setStyle(
                                "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");
                    }
                });

                modifierButtons.put(optionId, btn);
                FlowPane pane = categoryPanes.get(category);
                if (pane != null) {
                    pane.getChildren().add(btn);
                    if (category.equals("Topping")) {
                        toppingButtons.add(btn);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Fetch existing ingredients with quantities
        java.util.Map<Integer, Double> existingIngredients = new java.util.HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT item_id, quantity_used FROM ProductIngredient WHERE product_id = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    existingIngredients.put(rs.getInt("item_id"), rs.getDouble("quantity_used"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Base Ingredients Section
        Label ingredientsHeader = new Label("Base Ingredients (Inventory)");
        ingredientsHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        FlowPane ingredientsPane = new FlowPane();
        ingredientsPane.setHgap(10);
        ingredientsPane.setVgap(10);

        java.util.Map<String, Integer> inventoryMap = new java.util.HashMap<>();
        List<ToggleButton> ingredientButtons = new ArrayList<>();
        java.util.Map<String, TextField> ingredientQtyFields = new java.util.HashMap<>();

        String currentTeaItem = null;
        double currentTeaQty = 1.0;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT item_id, item_name FROM inventory ORDER BY item_name");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int itemId = rs.getInt("item_id");
                inventoryMap.put(itemName, itemId);

                if (itemName.toLowerCase().contains("tea leaves")) {
                    if (existingIngredients.containsKey(itemId)) {
                        currentTeaItem = itemName;
                        currentTeaQty = existingIngredients.get(itemId);
                    }
                } else {
                    ToggleButton btn = new ToggleButton(itemName);
                    btn.setPrefSize(100, 75);
                    btn.setWrapText(true);
                    btn.setStyle(
                            "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");

                    boolean isExisting = existingIngredients.containsKey(itemId);
                    double existingQty = isExisting ? existingIngredients.get(itemId) : 1.0;

                    TextField qtyField = new TextField(String.valueOf(existingQty));
                    qtyField.setPrefWidth(100);
                    qtyField.setPromptText("qty");
                    qtyField.setDisable(!isExisting);
                    qtyField.setStyle("-fx-font-size: 11; -fx-padding: 2 5 2 5;");
                    ingredientQtyFields.put(itemName, qtyField);

                    if (isExisting) {
                        btn.setSelected(true);
                        btn.setStyle(
                                "-fx-background-color: #e0f7fa; -fx-border-color: #00bcd4; -fx-background-radius: 5; -fx-border-radius: 5;");
                    }

                    btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
                        qtyField.setDisable(!newVal);
                        if (newVal) {
                            btn.setStyle(
                                    "-fx-background-color: #e0f7fa; -fx-border-color: #00bcd4; -fx-background-radius: 5; -fx-border-radius: 5;");
                        } else {
                            btn.setStyle(
                                    "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");
                        }
                    });
                    ingredientButtons.add(btn);
                    ingredientsPane.getChildren().add(new VBox(2, btn, qtyField));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Categories Dropdown Setup
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT DISTINCT category_name FROM product WHERE category_name IS NOT NULL ORDER BY category_name");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fetchedCategory = rs.getString("category_name");
                if (!categoryBox.getItems().contains(fetchedCategory)) {
                    categoryBox.getItems().add(fetchedCategory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Inventory and Labels
        Label invHeader = new Label("Inventory and Labels:");
        invHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        GridPane bottomGrid = new GridPane();
        bottomGrid.setHgap(20);
        bottomGrid.setVgap(15);

        Label teaBaseLbl = new Label("Tea Base:");
        teaBaseLbl.setStyle("-fx-font-weight: bold;");
        ComboBox<String> teaBaseBox = new ComboBox<>();
        teaBaseBox.setPrefWidth(200);
        teaBaseBox.setStyle(
                "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ccc; -fx-background-color: white;");
        for (String itemName : inventoryMap.keySet()) {
            if (itemName.toLowerCase().contains("tea leaves")) {
                teaBaseBox.getItems().add(itemName);
            }
        }
        if (currentTeaItem != null) {
            teaBaseBox.setValue(currentTeaItem);
        }

        Label teaQtyLbl = new Label("Tea Qty:");
        teaQtyLbl.setStyle("-fx-font-weight: bold;");
        TextField teaQtyField = new TextField(String.format("%.0f", currentTeaQty));
        teaQtyField.setPrefWidth(80);
        teaQtyField.setStyle(
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-padding: 4 8 4 8;");

        Label activeLbl = new Label("Active Item:");
        activeLbl.setStyle("-fx-font-weight: bold;");
        CheckBox activeCheck = new CheckBox();
        activeCheck.setSelected(currentIsActive);
        activeCheck.setStyle("-fx-scale-x: 1.5; -fx-scale-y: 1.5;");

        bottomGrid.add(teaBaseLbl, 0, 0);
        bottomGrid.add(teaBaseBox, 1, 0);
        bottomGrid.add(teaQtyLbl, 2, 0);
        bottomGrid.add(teaQtyField, 3, 0);
        bottomGrid.add(activeLbl, 0, 1);
        bottomGrid.add(activeCheck, 1, 1);

        content.getChildren().addAll(ingredientsHeader, ingredientsPane, invHeader, bottomGrid);

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(500);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: white;");

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newName = nameField.getText().trim();
            String newCategory = categoryBox.getValue();
            if (newCategory != null)
                newCategory = newCategory.trim();
            boolean newIsActive = activeCheck.isSelected();

            if (!newName.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceField.getText().trim());
                    String sql = "UPDATE product SET name = ?, base_price = ?, category_name = ?, is_active = ? WHERE product_id = ?";
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        conn.setAutoCommit(false);
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, newName);
                            pstmt.setDouble(2, newPrice);
                            pstmt.setString(3, newCategory);
                            pstmt.setBoolean(4, newIsActive);
                            pstmt.setInt(5, id);
                            pstmt.executeUpdate();

                            // Delete old relations
                            try (PreparedStatement d1 = conn
                                    .prepareStatement("DELETE FROM ProductModifier WHERE product_id = ?")) {
                                d1.setInt(1, id);
                                d1.executeUpdate();
                            }
                            try (PreparedStatement d2 = conn
                                    .prepareStatement("DELETE FROM ProductIngredient WHERE product_id = ?")) {
                                d2.setInt(1, id);
                                d2.executeUpdate();
                            }

                            // Insert new relations - Modifiers
                            List<Integer> finalModifiers = new ArrayList<>();
                            for (java.util.Map.Entry<Integer, ToggleButton> entry : modifierButtons.entrySet()) {
                                if (entry.getValue().isSelected())
                                    finalModifiers.add(entry.getKey());
                            }
                            if (!finalModifiers.isEmpty()) {
                                String modSql = "INSERT INTO ProductModifier (product_id, option_id) VALUES (?, ?)";
                                try (PreparedStatement modStmt = conn.prepareStatement(modSql)) {
                                    for (Integer modId : finalModifiers) {
                                        modStmt.setInt(1, id);
                                        modStmt.setInt(2, modId);
                                        modStmt.addBatch();
                                    }
                                    modStmt.executeBatch();
                                }
                            }

                            // Insert new relations - Ingredients
                            String selectedTea = teaBaseBox.getValue();
                            List<Integer> finalIngredients = new ArrayList<>();
                            List<Double> finalQuantities = new ArrayList<>();
                            if (selectedTea != null && inventoryMap.containsKey(selectedTea)) {
                                finalIngredients.add(inventoryMap.get(selectedTea));
                                double teaQty = 5.0;
                                try { teaQty = Double.parseDouble(teaQtyField.getText().trim()); } catch (NumberFormatException ignored) {}
                                finalQuantities.add(teaQty);
                            }
                            for (ToggleButton btn : ingredientButtons) {
                                if (btn.isSelected()) {
                                    Integer invId = inventoryMap.get(btn.getText());
                                    if (invId != null && !finalIngredients.contains(invId)) {
                                        finalIngredients.add(invId);
                                        double qty = 1.0;
                                        TextField qf = ingredientQtyFields.get(btn.getText());
                                        if (qf != null) {
                                            try { qty = Double.parseDouble(qf.getText().trim()); } catch (NumberFormatException ignored) {}
                                        }
                                        finalQuantities.add(qty);
                                    }
                                }
                            }
                            if (!finalIngredients.isEmpty()) {
                                String ingSql = "INSERT INTO ProductIngredient (product_id, item_id, quantity_used) VALUES (?, ?, ?)";
                                try (PreparedStatement ingStmt = conn.prepareStatement(ingSql)) {
                                    for (int i = 0; i < finalIngredients.size(); i++) {
                                        ingStmt.setInt(1, id);
                                        ingStmt.setInt(2, finalIngredients.get(i));
                                        ingStmt.setDouble(3, finalQuantities.get(i));
                                        ingStmt.addBatch();
                                    }
                                    ingStmt.executeBatch();
                                }
                            }

                            conn.commit();
                            loadMenuItems();
                        } catch (SQLException e) {
                            conn.rollback();
                            e.printStackTrace();
                        } finally {
                            conn.setAutoCommit(true);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the price.").showAndWait();
                }
            }
        }
    }

    private void handleRemoveMenuItem(int id, String name, HBox row) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Menu Item");
        confirm.setHeaderText("Remove " + name + "?");
        confirm.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                try (PreparedStatement del = conn.prepareStatement(
                        "DELETE FROM productmodifier WHERE product_id = ?")) {
                    del.setInt(1, id);
                    del.executeUpdate();
                }
                try (PreparedStatement del = conn.prepareStatement(
                        "DELETE FROM productingredient WHERE product_id = ?")) {
                    del.setInt(1, id);
                    del.executeUpdate();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "DELETE FROM product WHERE product_id = ?")) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                }
                allRows.remove(row);
                menuItemsList.getChildren().remove(row);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,
                        "Cannot delete \"" + name + "\" because it is referenced in existing orders.\n" +
                                "Consider deactivating it instead.")
                        .showAndWait();
            }
        }
    }

    @FXML
    void handleAddMenuItem(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Menu Item");
        dialog.setHeaderText("Manager Only - Add New Menu Item");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20, 20, 20, 20));
        content.setPrefWidth(700);

        // Name & Category
        GridPane topGrid = new GridPane();
        topGrid.setHgap(20);
        topGrid.setVgap(15);

        Label nameLbl = new Label("Drink Name:");
        nameLbl.setStyle("-fx-font-weight: bold;");
        TextField nameField = new TextField();
        nameField.setPrefWidth(400);
        nameField.setStyle(
                "-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #ccc; -fx-padding: 5 10 5 10;");

        Label catLbl = new Label("Category:");
        catLbl.setStyle("-fx-font-weight: bold;");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.setPrefWidth(400);
        categoryBox.setStyle(
                "-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #ccc; -fx-background-color: white;");
        categoryBox.setEditable(true);

        topGrid.add(nameLbl, 0, 0);
        topGrid.add(nameField, 1, 0);
        topGrid.add(catLbl, 0, 1);
        topGrid.add(categoryBox, 1, 1);

        // Pricing
        Label priceHeader = new Label("Pricing");
        priceHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        HBox priceBox = new HBox(15);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        Label priceLbl = new Label("Base Price: $");
        TextField priceField = new TextField();
        priceField.setPrefWidth(100);
        priceField.setStyle(
                "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ccc; -fx-padding: 5 10 5 10;");
        priceBox.getChildren().addAll(priceLbl, priceField);

        // Modifiers Sections
        Label modifiersHeader = new Label("Modifiers");
        modifiersHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        content.getChildren().addAll(topGrid, priceHeader, priceBox, modifiersHeader);

        // Map to hold our toggle buttons for modifiers, keyed by option_id
        java.util.Map<Integer, ToggleButton> modifierButtons = new java.util.HashMap<>();

        // Structures to group modifier buttons by category
        java.util.Map<String, FlowPane> categoryPanes = new java.util.HashMap<>();
        List<ToggleButton> toppingButtons = new ArrayList<>();

        String[] categories = { "Topping", "Milk Type", "Sugar Level", "Ice Level", "Size" };
        for (String cat : categories) {
            Label modCatLbl = new Label(cat + ":");
            modCatLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            FlowPane pane = new FlowPane();
            pane.setHgap(10);
            pane.setVgap(10);
            categoryPanes.put(cat, pane);

            if (cat.equals("Topping")) {
                HBox catHeader = new HBox(15);
                catHeader.setAlignment(Pos.CENTER_LEFT);
                Button toggleAllBtn = new Button("Toggle All");
                toggleAllBtn.setStyle(
                        "-fx-background-color: transparent; -fx-border-color: #00bcd4; -fx-text-fill: #00bcd4; -fx-border-radius: 10; -fx-background-radius: 10;");
                toggleAllBtn.setOnAction(e -> {
                    boolean allSelected = toppingButtons.stream().allMatch(ToggleButton::isSelected);
                    for (ToggleButton tb : toppingButtons) {
                        tb.setSelected(!allSelected);
                    }
                });
                catHeader.getChildren().addAll(modCatLbl, toggleAllBtn);
                content.getChildren().addAll(catHeader, pane);
            } else {
                content.getChildren().addAll(modCatLbl, pane);
            }
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT option_id, name, category, price_adjustment FROM ModifierOption ORDER BY category, name");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int optionId = rs.getInt("option_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double priceAdj = rs.getDouble("price_adjustment");

                String btnText = name + (priceAdj > 0 ? String.format(" (+$%.2f)", priceAdj) : "");
                ToggleButton btn = new ToggleButton(btnText);
                btn.setPrefSize(120, 50);
                btn.setWrapText(true);
                btn.setStyle(
                        "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");

                btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal) {
                        btn.setStyle(
                                "-fx-background-color: #fff3e0; -fx-border-color: #ff9800; -fx-background-radius: 5; -fx-border-radius: 5;");
                    } else {
                        btn.setStyle(
                                "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");
                    }
                });

                modifierButtons.put(optionId, btn);

                FlowPane pane = categoryPanes.get(category);
                if (pane != null) {
                    pane.getChildren().add(btn);
                    if (category.equals("Topping")) {
                        toppingButtons.add(btn);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Base Ingredients Section
        Label ingredientsHeader = new Label("Base Ingredients (Inventory)");
        ingredientsHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        FlowPane ingredientsPane = new FlowPane();
        ingredientsPane.setHgap(10);
        ingredientsPane.setVgap(10);

        java.util.Map<String, Integer> inventoryMap = new java.util.HashMap<>();
        List<ToggleButton> ingredientButtons = new ArrayList<>();
        java.util.Map<String, TextField> ingredientQtyFields = new java.util.HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT item_id, item_name FROM inventory ORDER BY item_name");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                inventoryMap.put(itemName, rs.getInt("item_id"));

                // Only add to base ingredients if it's NOT a tea base
                if (!itemName.toLowerCase().contains("tea leaves")) {
                    ToggleButton btn = new ToggleButton(itemName);
                    btn.setPrefSize(100, 75);
                    btn.setWrapText(true);
                    btn.setStyle(
                            "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");

                    TextField qtyField = new TextField("1");
                    qtyField.setPrefWidth(100);
                    qtyField.setPromptText("qty");
                    qtyField.setDisable(true);
                    qtyField.setStyle("-fx-font-size: 11; -fx-padding: 2 5 2 5;");
                    ingredientQtyFields.put(itemName, qtyField);

                    btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
                        qtyField.setDisable(!newVal);
                        if (newVal) {
                            btn.setStyle(
                                    "-fx-background-color: #e0f7fa; -fx-border-color: #00bcd4; -fx-background-radius: 5; -fx-border-radius: 5;");
                        } else {
                            btn.setStyle(
                                    "-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");
                        }
                    });
                    ingredientButtons.add(btn);
                    ingredientsPane.getChildren().add(new VBox(2, btn, qtyField));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT DISTINCT category_name FROM product WHERE category_name IS NOT NULL ORDER BY category_name");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categoryBox.getItems().add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Inventory and Labels
        Label invHeader = new Label("Inventory and Labels:");
        invHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        GridPane bottomGrid = new GridPane();
        bottomGrid.setHgap(20);
        bottomGrid.setVgap(15);

        Label teaBaseLbl = new Label("Tea Base:");
        teaBaseLbl.setStyle("-fx-font-weight: bold;");
        ComboBox<String> teaBaseBox = new ComboBox<>();
        teaBaseBox.setPrefWidth(200);
        teaBaseBox.setStyle(
                "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ccc; -fx-background-color: white;");
        for (String itemName : inventoryMap.keySet()) {
            if (itemName.toLowerCase().contains("tea leaves")) {
                teaBaseBox.getItems().add(itemName);
            }
        }

        Label teaQtyLbl = new Label("Tea Qty:");
        teaQtyLbl.setStyle("-fx-font-weight: bold;");
        TextField teaQtyField = new TextField("5");
        teaQtyField.setPrefWidth(80);
        teaQtyField.setStyle(
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-padding: 4 8 4 8;");

        Label activeLbl = new Label("Active Item:");
        activeLbl.setStyle("-fx-font-weight: bold;");
        CheckBox activeCheck = new CheckBox();
        activeCheck.setSelected(true);
        activeCheck.setStyle("-fx-scale-x: 1.5; -fx-scale-y: 1.5;");

        bottomGrid.add(teaBaseLbl, 0, 0);
        bottomGrid.add(teaBaseBox, 1, 0);
        bottomGrid.add(teaQtyLbl, 2, 0);
        bottomGrid.add(teaQtyField, 3, 0);
        bottomGrid.add(activeLbl, 0, 1);
        bottomGrid.add(activeCheck, 1, 1);

        content.getChildren().addAll(ingredientsHeader, ingredientsPane, invHeader, bottomGrid);

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(500);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: white;");

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText().trim();
            String category = categoryBox.getValue();
            if (category != null)
                category = category.trim();
            boolean isActive = activeCheck.isSelected();

            if (!name.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceField.getText().trim());
                    String sql = "INSERT INTO product (name, base_price, category_name, is_active) VALUES (?, ?, ?, ?) RETURNING product_id";
                    try (Connection conn = DatabaseConnection.getConnection();
                            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setDouble(2, price);
                        pstmt.setString(3, category);
                        pstmt.setBoolean(4, isActive);

                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            int newProductId = rs.getInt(1);

                            String selectedTea = teaBaseBox.getValue();
                            List<Integer> finalIngredients = new ArrayList<>();
                            List<Double> finalQuantities = new ArrayList<>();
                            if (selectedTea != null && inventoryMap.containsKey(selectedTea)) {
                                finalIngredients.add(inventoryMap.get(selectedTea));
                                double teaQty = 5.0;
                                try { teaQty = Double.parseDouble(teaQtyField.getText().trim()); } catch (NumberFormatException ignored) {}
                                finalQuantities.add(teaQty);
                            }
                            for (ToggleButton btn : ingredientButtons) {
                                if (btn.isSelected()) {
                                    Integer id = inventoryMap.get(btn.getText());
                                    if (id != null && !finalIngredients.contains(id)) {
                                        finalIngredients.add(id);
                                        double qty = 1.0;
                                        TextField qf = ingredientQtyFields.get(btn.getText());
                                        if (qf != null) {
                                            try { qty = Double.parseDouble(qf.getText().trim()); } catch (NumberFormatException ignored) {}
                                        }
                                        finalQuantities.add(qty);
                                    }
                                }
                            }

                            if (!finalIngredients.isEmpty()) {
                                String ingredientSql = "INSERT INTO productingredient (product_id, item_id, quantity_used) VALUES (?, ?, ?)";
                                try (PreparedStatement ingStmt = conn.prepareStatement(ingredientSql)) {
                                    for (int i = 0; i < finalIngredients.size(); i++) {
                                        ingStmt.setInt(1, newProductId);
                                        ingStmt.setInt(2, finalIngredients.get(i));
                                        ingStmt.setDouble(3, finalQuantities.get(i));
                                        ingStmt.addBatch();
                                    }
                                    ingStmt.executeBatch();
                                }
                            }

                            // Insert selected modifiers
                            List<Integer> finalModifiers = new ArrayList<>();
                            for (java.util.Map.Entry<Integer, ToggleButton> entry : modifierButtons.entrySet()) {
                                if (entry.getValue().isSelected()) {
                                    finalModifiers.add(entry.getKey());
                                }
                            }

                            if (!finalModifiers.isEmpty()) {
                                String modSql = "INSERT INTO ProductModifier (product_id, option_id) VALUES (?, ?)";
                                try (PreparedStatement modStmt = conn.prepareStatement(modSql)) {
                                    for (Integer modId : finalModifiers) {
                                        modStmt.setInt(1, newProductId);
                                        modStmt.setInt(2, modId);
                                        modStmt.addBatch();
                                    }
                                    modStmt.executeBatch();
                                }
                            }
                        }
                        loadMenuItems();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the price.").showAndWait();
                }
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
    void handleNavigateEmployees(ActionEvent event) {
        navigateTo("employee-list-view.fxml");
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
            switchScene(root, fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(Parent root, String fxmlFile) {
        Stage stage = (Stage) menuItemsList.getScene().getWindow();
        double width = SceneConfig.isLoginView(fxmlFile) ? SceneConfig.LOGIN_WIDTH : SceneConfig.APP_WIDTH;
        double height = SceneConfig.isLoginView(fxmlFile) ? SceneConfig.LOGIN_HEIGHT : SceneConfig.APP_HEIGHT;
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
