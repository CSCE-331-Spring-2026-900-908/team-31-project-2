package com.example.team31project2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InventoryViewController {

    @FXML
    private MenuBar menuBar;

    @FXML
    void handleNavigateOrdering(ActionEvent event) {
        navigateTo("ordering-view.fxml");
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
    void handleNavigateReports(ActionEvent event) {
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

            Stage stage = (Stage) menuBar.getScene().getWindow();
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

    //TABLE VALUES
    @FXML private TableView<InventoryItem> table;

    @FXML private TableColumn<InventoryItem, Number> colId;
    @FXML private TableColumn<InventoryItem, String> colName;
    @FXML private TableColumn<InventoryItem, Double> colQty;
    @FXML private TableColumn<InventoryItem, String> colUnit;
    @FXML private TableColumn<InventoryItem, Double> colFill;
    @FXML private TableColumn<InventoryItem, Double> colTarg;

    //TEXT FIELDS
    @FXML private javafx.scene.control.TextField textName;
    @FXML private javafx.scene.control.TextField textQty;
    @FXML private javafx.scene.control.TextField textUnit;
    @FXML private javafx.scene.control.DatePicker textdate;
    @FXML private javafx.scene.control.TextField textTarg;

    public void initialize(){
        //LINK THE DB TO THE ROWS
        table.setEditable(true);

        colId.setCellValueFactory(data -> data.getValue().itemIdProperty());
        colName.setCellValueFactory(data -> data.getValue().itemNameProperty());
        colQty.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        colUnit.setCellValueFactory(data -> data.getValue().unitTypeProperty());
        colTarg.setCellValueFactory(data -> data.getValue().targetProperty().asObject());
        colFill.setCellValueFactory(data -> data.getValue().fillRatioProperty().asObject());

        colQty.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colTarg.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colFill.setCellFactory(column -> new TableCell<InventoryItem, Double>() {
            private final ProgressBar bar = new ProgressBar(0);
            private final Label label = new Label();
            private final HBox box = new HBox(8, bar, label);

            {
                bar.setMaxWidth(Double.MAX_VALUE);
                box.setFillHeight(true);
            }

            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                double ratio = Math.max(0.0, Math.min(1.0, value.doubleValue()));
                bar.setProgress(ratio);
                label.setText(String.format("%.0f%%", ratio * 100.0));
                setGraphic(box);
                setText(null);
            }
        });
        
        //MAKE ROWS QTY AND TARGET EDITABLE 
        colQty.setOnEditCommit(event -> {
        InventoryItem item = event.getRowValue();
        Double newVal = event.getNewValue();
        Double oldVal = event.getOldValue();

        if (newVal == null || newVal < 0) { // simple validation
            table.refresh();
            return;
        }

        item.setQuantity(newVal);
        boolean ok = updateQuantityInDb(item.getItemId(), newVal);

        if (!ok) {          // revert on failure
            item.setQuantity(oldVal);
            table.refresh();
        }
        });

        colTarg.setOnEditCommit(event-> {
            InventoryItem item = event.getRowValue(); 
            Double newVal = event.getNewValue();
            Double oldVal = event.getOldValue(); 

            if(newVal == null || newVal < 0){
                table.refresh();
                return;
            }

            item.setTarget(newVal);
            boolean ok = updateTargetInDb(item.getItemId(), newVal);

            if (!ok) {          // revert on failure
            item.setTarget(oldVal);
            table.refresh();
        }
        });

        

        table.setItems(inventoryList);
        loadInventoryFromDB(); // LOADS ALL ITEMS FROM DB TO IT
        
    }

    //LOADING FUNCTION
    private final ObservableList<InventoryItem> inventoryList = FXCollections.observableArrayList();

    private void loadInventoryFromDB(){
        try(Connection conn = DatabaseConnection.getConnection()){
            InventoryDAO dao  = new InventoryDAO(conn);
            inventoryList.setAll(dao.getAllItems());
            table.setItems(inventoryList);
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //UPADATE QUANITY FUNCTION
    private boolean updateQuantityInDb(int itemId, Double newCurrent) {
    String sql = "UPDATE inventory SET quantity = ? WHERE item_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDouble(1, newCurrent);
        ps.setInt(2, itemId);

        return ps.executeUpdate() == 1; // true if one row updated
    } catch (Exception e) {
        e.printStackTrace();
        return false;
        }
    }

    private boolean updateTargetInDb (int itemId, Double newTarget){
        String sql = "UPDATE inventory SET target_val = ? WHERE item_id = ?";
        try(Connection conn = DatabaseConnection.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, newTarget);
            ps.setInt(2, itemId);
            return ps.executeUpdate() == 1; 
        }catch (Exception e) {
        e.printStackTrace();
        return false;
        }
    }

    @FXML
    private void addHandelr(){
        String name = textName.getText().trim();
        Double quantity = Double.parseDouble(textQty.getText().trim());
        double target = Double.parseDouble(textTarg.getText().trim());
        String unit = textUnit.getText().trim();

        if (name.isEmpty() || unit.isEmpty()) return;
        if (target < 0 || quantity < 0) return;

        java.time.LocalDateTime exp = null;
        if (textdate != null && textdate.getValue() != null) {
            exp = textdate.getValue().atStartOfDay(); // midnight
        }

        Integer id = insertItemInDb(name, quantity, unit, exp, target);
        if (id == null) return;


        InventoryItem newItem = new InventoryItem(id, name, quantity, unit, exp, target);
        inventoryList.add(newItem);
        inventoryList.sort((a, b) -> Integer.compare(a.getItemId(), b.getItemId()));

        textName.clear();
        textQty.clear();
        textTarg.clear();
        textUnit.clear();
        if (textdate != null) textdate.setValue(null);
    }

    private Integer insertItemInDb(String name, double quantity, String unit, java.time.LocalDateTime exp, double target){
        String sql = "INSERT INTO inventory (item_name, quantity, unit_type, expiration_date, target_val) VALUES (?, ?, ?, ?, ?) RETURNING item_id";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){ 
        ps.setString(1, name);
        ps.setDouble(2, quantity);
        ps.setString(3, unit);

        if (exp == null) ps.setTimestamp(4, null);
        else ps.setTimestamp(4, java.sql.Timestamp.valueOf(exp));

        ps.setDouble(5, target);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("item_id");
            }
        }

        return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
                        
    }

    @FXML
    private void removeHandler(){
        try {
        InventoryItem selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        int id = selectedItem.getItemId();
        
        if(id < 0 ){
            return;
        }

        boolean ok = removeFromDB(id);
        if(!ok){return;}
        inventoryList.removeIf(item -> item.getItemId() == id);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean removeFromDB(int id){
        String sql = "DELETE FROM inventory WHERE item_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);

            return ps.executeUpdate() == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
